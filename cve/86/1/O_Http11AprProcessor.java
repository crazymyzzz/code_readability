

/**
 * Processes HTTP requests.
 *
 * @author Remy Maucherat
 */
public class Http11AprProcessor extends AbstractHttp11Processor<Long> {



    
    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    @Override
    public SocketState process(SocketWrapper<Long> socket)
        throws IOException {
        RequestInfo rp = request.getRequestProcessor();
        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE);

        // Setting up the socket
        this.socket = socket;
        long socketRef = socket.getSocket().longValue();
        inputBuffer.setSocket(socketRef);
        outputBuffer.setSocket(socketRef);

        // Error flag
        error = false;
        comet = false;
        keepAlive = true;

        int keepAliveLeft = maxKeepAliveRequests;
        long soTimeout = endpoint.getSoTimeout();
        
        boolean keptAlive = false;
        boolean openSocket = false;

        while (!error && keepAlive && !comet && !isAsync() && !endpoint.isPaused()) {

            // Parsing the request header
            try {
                if( !disableUploadTimeout && keptAlive && soTimeout > 0 ) {
                    Socket.timeoutSet(socketRef, soTimeout * 1000);
                }
                if (!inputBuffer.parseRequestLine(keptAlive)) {
                    // This means that no data is available right now
                    // (long keepalive), so that the processor should be recycled
                    // and the method should return true
                    openSocket = true;
                    if (endpoint.isPaused()) {
                        // 503 - Service unavailable
                        response.setStatus(503);
                        adapter.log(request, response, 0);
                        error = true;
                    } else {
                        break;
                    }
                }
                if (!endpoint.isPaused()) {
                    request.setStartTime(System.currentTimeMillis());
                    keptAlive = true;
                    if (!disableUploadTimeout) {
                        Socket.timeoutSet(socketRef,
                                connectionUploadTimeout * 1000);
                    }
                    inputBuffer.parseHeaders();
                }
            } catch (IOException e) {
                error = true;
                break;
            } catch (Throwable t) {
                ExceptionUtils.handleThrowable(t);
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.header.parse"), t);
                }
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
                    if (log.isDebugEnabled()) {
                        log.debug(sm.getString("http11processor.request.prepare"), t);
                    }
                    // 400 - Internal Server Error
                    response.setStatus(400);
                    adapter.log(request, response, 0);
                    error = true;
                }
            }

            if (maxKeepAliveRequests > 0 && --keepAliveLeft == 0)
                keepAlive = false;

            // Process the request in the adapter
            if (!error) {
                try {
                    rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
                    adapter.service(request, response);
                    // Handle when the response was committed before a serious
                    // error occurred.  Throwing a ServletException should both
                    // set the status to 500 and set the errorException.
                    // If we fail here, then the response is likely already
                    // committed, so we can't try and set headers.
                    if(keepAlive && !error) { // Avoid checking twice.
                        error = response.getErrorException() != null ||
                                (!isAsync() &&
                                statusDropsConnection(response.getStatus()));
                    }
                } catch (InterruptedIOException e) {
                    error = true;
                } catch (Throwable t) {
                    ExceptionUtils.handleThrowable(t);
                    log.error(sm.getString("http11processor.request.process"), t);
                    // 500 - Internal Server Error
                    response.setStatus(500);
                    adapter.log(request, response, 0);
                    error = true;
                }
            }

            // Finish the handling of the request
            if (!comet && !isAsync()) {
                // If we know we are closing the connection, don't drain input.
                // This way uploading a 100GB file doesn't tie up the thread 
                // if the servlet has rejected it.
                if(error)
                    inputBuffer.setSwallowInput(false);
                endRequest();
            }

            // If there was an error, make sure the request is counted as
            // and error, and update the statistics counter
            if (error) {
                response.setStatus(500);
            }
            request.updateCounters();

            if (!comet && !isAsync()) {
                // Next request
                inputBuffer.nextRequest();
                outputBuffer.nextRequest();
            }
            
            // Do sendfile as needed: add socket to sendfile and end
            if (sendfileData != null && !error) {
                sendfileData.socket = socketRef;
                sendfileData.keepAlive = keepAlive;
                if (!((AprEndpoint)endpoint).getSendfile().add(sendfileData)) {
                    openSocket = true;
                    break;
                }
            }
            
            rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);

        }

        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);

        if (error || endpoint.isPaused()) {
            return SocketState.CLOSED;
        } else if (comet  || isAsync()) {
            return SocketState.LONG;
        } else {
            return (openSocket) ? SocketState.OPEN : SocketState.CLOSED;
        }
        
    }


    
}
