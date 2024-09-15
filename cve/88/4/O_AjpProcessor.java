


/**
 * Processes AJP requests.
 *
 * @author Remy Maucherat
 * @author Henri Gomez
 * @author Dan Milstein
 * @author Keith Wannamaker
 * @author Kevin Seguin
 * @author Costin Manolache
 * @author Bill Barker
 */
public class AjpProcessor extends AbstractAjpProcessor<Socket> {




    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    @Override
    public SocketState process(SocketWrapper<Socket> socket)
        throws IOException {
        RequestInfo rp = request.getRequestProcessor();
        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE);

        // Setting up the socket
        this.socket = socket;
        input = socket.getSocket().getInputStream();
        output = socket.getSocket().getOutputStream();
        int soTimeout = -1;
        if (keepAliveTimeout > 0) {
            soTimeout = socket.getSocket().getSoTimeout();
        }

        // Error flag
        error = false;

        while (!error && !endpoint.isPaused()) {

            // Parsing the request header
            try {
                // Set keep alive timeout if enabled
                if (keepAliveTimeout > 0) {
                    socket.getSocket().setSoTimeout(keepAliveTimeout);
                }
                // Get first message of the request
                if (!readMessage(requestHeaderMessage)) {
                    // This means a connection timeout
                    break;
                }
                // Set back timeout if keep alive timeout is enabled
                if (keepAliveTimeout > 0) {
                    socket.getSocket().setSoTimeout(soTimeout);
                }
                // Check message type, process right away and break if
                // not regular request processing
                int type = requestHeaderMessage.getByte();
                if (type == Constants.JK_AJP13_CPING_REQUEST) {
                    try {
                        output.write(pongMessageArray);
                    } catch (IOException e) {
                        error = true;
                    }
                    continue;
                } else if(type != Constants.JK_AJP13_FORWARD_REQUEST) {
                    // Usually the servlet didn't read the previous request body
                    if(log.isDebugEnabled()) {
                        log.debug("Unexpected message: "+type);
                    }
                    continue;
                }

                request.setStartTime(System.currentTimeMillis());
            } catch (IOException e) {
                error = true;
                break;
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                log.debug(sm.getString("ajpprocessor.header.error"), t);
                // 400 - Bad Request
                response.setStatus(400);
                adapter.log(request, response, 0);
                error = true;
            }

            if (!error) {
                // Setting up filters, and parse some request headers
                rp.setStage(org.apache.coyote.Constants.STAGE_PREPARE);
                try {
                    prepareRequest();
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.debug(sm.getString("ajpprocessor.request.prepare"), t);
                    // 400 - Internal Server Error
                    response.setStatus(400);
                    adapter.log(request, response, 0);
                    error = true;
                }
            }

            if (endpoint.isPaused()) {
                // 503 - Service unavailable
                response.setStatus(503);
                adapter.log(request, response, 0);
                error = true;
            }

            // Process the request in the adapter
            if (!error) {
                try {
                    rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
                    adapter.service(request, response);
                } catch (InterruptedIOException e) {
                    error = true;
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.error(sm.getString("ajpprocessor.request.process"), t);
                    // 500 - Internal Server Error
                    response.setStatus(500);
                    adapter.log(request, response, 0);
                    error = true;
                }
            }
            
            if (isAsync() && !error) {
                break;
            }

            // Finish the response if not done yet
            if (!finished) {
                try {
                    finish();
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    error = true;
                }
            }

            // If there was an error, make sure the request is counted as
            // and error, and update the statistics counter
            if (error) {
                response.setStatus(500);
            }
            request.updateCounters();

            rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);
            recycle(false);
        }
        
        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);

        if (isAsync() && !error && !endpoint.isPaused()) {
            return SocketState.LONG;
        } else {
            input = null;
            output = null;
            return SocketState.CLOSED;
        }
        
    }

    
}
