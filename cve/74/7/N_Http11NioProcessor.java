

/**
 * Processes HTTP requests.
 *
 * @author Remy Maucherat
 * @author Filip Hanik
 */
public class Http11NioProcessor extends AbstractHttp11Processor {




    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    public SocketState event(SocketStatus status)
        throws IOException {

        long soTimeout = endpoint.getSoTimeout();
        int keepAliveTimeout = endpoint.getKeepAliveTimeout();

        RequestInfo rp = request.getRequestProcessor();
        final NioEndpoint.KeyAttachment attach = (NioEndpoint.KeyAttachment)socket.getAttachment(false);
        try {
            rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
            error = !adapter.event(request, response, status);
            if ( !error ) {
                if (attach != null) {
                    attach.setComet(comet);
                    if (comet) {
                        Integer comettimeout = (Integer) request.getAttribute("org.apache.tomcat.comet.timeout");
                        if (comettimeout != null) attach.setTimeout(comettimeout.longValue());
                    } else {
                        //reset the timeout
                        if (keepAlive && keepAliveTimeout>0) {
                            attach.setTimeout(keepAliveTimeout);
                        } else {
                            attach.setTimeout(soTimeout);
                        }
                    }

                }
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

        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);

        if (error) {
            return SocketState.CLOSED;
        } else if (!comet) {
            return (keepAlive)?SocketState.OPEN:SocketState.CLOSED;
        } else {
            return SocketState.LONG;
        }
    }
    
    
    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    public SocketState asyncDispatch(SocketStatus status)
        throws IOException {

        long soTimeout = endpoint.getSoTimeout();
        int keepAliveTimeout = endpoint.getKeepAliveTimeout();

        RequestInfo rp = request.getRequestProcessor();
        final NioEndpoint.KeyAttachment attach = (NioEndpoint.KeyAttachment)socket.getAttachment(false);
        try {
            rp.setStage(org.apache.coyote.Constants.STAGE_SERVICE);
            error = !adapter.asyncDispatch(request, response, status);
            if ( !error ) {
                if (attach != null) {
                    attach.setComet(comet);
                    if (comet) {
                        Integer comettimeout = (Integer) request.getAttribute("org.apache.tomcat.comet.timeout");
                        if (comettimeout != null) attach.setTimeout(comettimeout.longValue());
                    } else {
                        if (asyncStateMachine.isAsyncDispatching()) {
                            //reset the timeout
                            if (keepAlive && keepAliveTimeout>0) {
                                attach.setTimeout(keepAliveTimeout);
                            } else {
                                attach.setTimeout(soTimeout);
                            }
                        }
                    }

                }
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

        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);

        if (error) {
            return SocketState.CLOSED;
        } else if (!comet && !isAsync()) {
            return (keepAlive)?SocketState.OPEN:SocketState.CLOSED;
        } else {
            return SocketState.LONG;
        }
    }

    /**
     * Process pipelined HTTP requests using the specified input and output
     * streams.
     *
     * @throws IOException error during an I/O operation
     */
    public SocketState process(NioChannel socket)
        throws IOException {
        RequestInfo rp = request.getRequestProcessor();
        rp.setStage(org.apache.coyote.Constants.STAGE_PARSE);

        // Setting up the socket
        this.socket = socket;
        inputBuffer.setSocket(socket);
        outputBuffer.setSocket(socket);
        inputBuffer.setSelectorPool(endpoint.getSelectorPool());
        outputBuffer.setSelectorPool(endpoint.getSelectorPool());

        // Error flag
        error = false;
        keepAlive = true;
        comet = false;
        
        long soTimeout = endpoint.getSoTimeout();
        int keepAliveTimeout = endpoint.getKeepAliveTimeout();

        boolean keptAlive = false;
        boolean openSocket = false;
        boolean readComplete = true;
        final KeyAttachment ka = (KeyAttachment)socket.getAttachment(false);
        
        while (!error && keepAlive && !comet && !isAsync() && !endpoint.isPaused()) {
            //always default to our soTimeout
            ka.setTimeout(soTimeout);
            // Parsing the request header
            try {
                if( !disableUploadTimeout && keptAlive && soTimeout > 0 ) {
                    socket.getIOChannel().socket().setSoTimeout((int)soTimeout);
                }
                if (!inputBuffer.parseRequestLine(keptAlive)) {
                    // Haven't finished reading the request so keep the socket
                    // open
                    openSocket = true;
                    // Check to see if we have read any of the request line yet
                    if (inputBuffer.getParsingRequestLinePhase()<2) {
                        // No data read, OK to recycle the processor
                        // Continue to use keep alive timeout
                        if (keepAliveTimeout>0) ka.setTimeout(keepAliveTimeout);
                    } else {
                        // Started to read request line. Need to keep processor
                        // associated with socket
                        readComplete = false;
                    }
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
                    keptAlive = true;
                    if ( !inputBuffer.parseHeaders() ) {
                        //we've read part of the request, don't recycle it
                        //instead associate it with the socket
                        openSocket = true;
                        readComplete = false;
                        break;
                    }
                    request.setStartTime(System.currentTimeMillis());
                    if (!disableUploadTimeout) { //only for body, not for request headers
                        socket.getIOChannel().socket().setSoTimeout(
                                connectionUploadTimeout);
                    }
                }
            } catch (IOException e) {
                if (log.isDebugEnabled()) {
                    log.debug(sm.getString("http11processor.header.parse"), e);
                }
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
            
            if (maxKeepAliveRequests == 1 )
                keepAlive = false;
            if (maxKeepAliveRequests > 0 && ka.decrementKeepAlive() <= 0)
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
                                statusDropsConnection(response.getStatus());
                    }
                    // Comet support
                    SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
                    if (key != null) {
                        NioEndpoint.KeyAttachment attach = (NioEndpoint.KeyAttachment) key.attachment();
                        if (attach != null)  {
                            attach.setComet(comet);
                            if (comet) {
                                Integer comettimeout = (Integer) request.getAttribute("org.apache.tomcat.comet.timeout");
                                if (comettimeout != null) attach.setTimeout(comettimeout.longValue());
                            }
                        }
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
                ka.setSendfileData(sendfileData);
                sendfileData.keepAlive = keepAlive;
                SelectionKey key = socket.getIOChannel().keyFor(socket.getPoller().getSelector());
                //do the first write on this thread, might as well
                openSocket = socket.getPoller().processSendfile(key,ka,true,true);
                break;
            }


            rp.setStage(org.apache.coyote.Constants.STAGE_KEEPALIVE);

        }//while

        rp.setStage(org.apache.coyote.Constants.STAGE_ENDED);
        if (error || endpoint.isPaused()) {
            return SocketState.CLOSED;
        } else if (comet || isAsync()) {
            return SocketState.LONG;
        } else {
            return (openSocket) ? (readComplete?SocketState.OPEN:SocketState.LONG) : SocketState.CLOSED;
        }

    }


   
}
