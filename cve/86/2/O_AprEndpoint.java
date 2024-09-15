


/**
 * APR tailored thread pool, providing the following services:
 * <ul>
 * <li>Socket acceptor thread</li>
 * <li>Socket poller thread</li>
 * <li>Sendfile thread</li>
 * <li>Worker threads pool</li>
 * </ul>
 *
 * When switching to Java 5, there's an opportunity to use the virtual
 * machine's thread pool.
 *
 * @author Mladen Turk
 * @author Remy Maucherat
 */
public class AprEndpoint extends AbstractEndpoint {



        /**
         * Add the sendfile data to the sendfile poller. Note that in most cases,
         * the initial non blocking calls to sendfile will return right away, and
         * will be handled asynchronously inside the kernel. As a result,
         * the poller will never be used.
         *
         * @param data containing the reference to the data which should be sent
         * @return true if all the data has been sent right away, and false
         *              otherwise
         */
        public boolean add(SendfileData data) {
            // Initialize fd from data given
            try {
                data.fdpool = Socket.pool(data.socket);
                data.fd = File.open
                    (data.fileName, File.APR_FOPEN_READ
                     | File.APR_FOPEN_SENDFILE_ENABLED | File.APR_FOPEN_BINARY,
                     0, data.fdpool);
                data.pos = data.start;
                // Set the socket to nonblocking mode
                Socket.timeoutSet(data.socket, 0);
                while (true) {
                    long nw = Socket.sendfilen(data.socket, data.fd,
                                               data.pos, data.end - data.pos, 0);
                    if (nw < 0) {
                        if (!(-nw == Status.EAGAIN)) {
                            destroySocket(data.socket);
                            data.socket = 0;
                            return false;
                        } else {
                            // Break the loop and add the socket to poller.
                            break;
                        }
                    }

                    data.pos = data.pos + nw;
                    if (data.pos >= data.end) {
                        // Entire file has been sent
                        Pool.destroy(data.fdpool);
                        // Set back socket to blocking mode
                        Socket.timeoutSet(data.socket, socketProperties.getSoTimeout() * 1000);
                        return true;
                    }
                }
            } catch (Exception e) {
                log.error(sm.getString("endpoint.sendfile.error"), e);
                return false;
            }
            // Add socket to the list. Newly added sockets will wait
            // at most for pollTime before being polled
            synchronized (this) {
                addS.add(data);
                addCount++;
                this.notify();
            }
            return false;
        }

       
}
