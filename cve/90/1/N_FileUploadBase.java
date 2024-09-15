


/**
 * <p>High level API for processing file uploads.</p>
 *
 * <p>This class handles multiple files per single HTML widget, sent using
 * <code>multipart/mixed</code> encoding type, as specified by
 * <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>.  Use {@link
 * #parseRequest(RequestContext)} to acquire a list of {@link
 * org.apache.tomcat.util.http.fileupload.FileItem}s associated with a given HTML
 * widget.</p>
 *
 * <p>How the data for individual parts is stored is determined by the factory
 * used to create them; a given part may be in memory, on disk, or somewhere
 * else.</p>
 */
public abstract class FileUploadBase {


        /**
         * Creates a new instance.
         *
         * @param ctx The request context.
         * @throws FileUploadException An error occurred while
         *   parsing the request.
         * @throws IOException An I/O error occurred.
         */
        FileItemIteratorImpl(RequestContext ctx)
                throws FileUploadException, IOException {
            if (ctx == null) {
                throw new NullPointerException("ctx parameter");
            }

            String contentType = ctx.getContentType();
            if ((null == contentType)
                    || (!contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART))) {
                throw new InvalidContentTypeException(String.format(
                        "the request doesn't contain a %s or %s stream, content type header is %s",
                        MULTIPART_FORM_DATA, MULTIPART_MIXED, contentType));
            }

            InputStream input = ctx.getInputStream();

            final long requestSize = ((UploadContext) ctx).contentLength();

            if (sizeMax >= 0) {
                if (requestSize != -1 && requestSize > sizeMax) {
                    throw new SizeLimitExceededException(String.format(
                            "the request was rejected because its size (%s) exceeds the configured maximum (%s)",
                            Long.valueOf(requestSize), Long.valueOf(sizeMax)),
                            requestSize, sizeMax);
                }
                input = new LimitedInputStream(input, sizeMax) {
                    @Override
                    protected void raiseError(long pSizeMax, long pCount)
                            throws IOException {
                        FileUploadException ex = new SizeLimitExceededException(
                        String.format("the request was rejected because its size (%s) exceeds the configured maximum (%s)",
                               Long.valueOf(pCount), Long.valueOf(pSizeMax)),
                               pCount, pSizeMax);
                        throw new FileUploadIOException(ex);
                    }
                };
            }

            String charEncoding = headerEncoding;
            if (charEncoding == null) {
                charEncoding = ctx.getCharacterEncoding();
            }

            boundary = getBoundary(contentType);
            if (boundary == null) {
                throw new FileUploadException("the request was rejected because no multipart boundary was found");
            }

            notifier = new MultipartStream.ProgressNotifier(listener, requestSize);
            try {
                multi = new MultipartStream(input, boundary, notifier);
            } catch (IllegalArgumentException iae) {
                throw new InvalidContentTypeException(String.format(
                        "The boundary specified in the %s header is too long",
                        CONTENT_TYPE), iae);
            }
            multi.setHeaderEncoding(charEncoding);

            skipPreamble = true;
            findNextItem();
        }





        /**
         * Constructs a <code>InvalidContentTypeException</code> with no
         * detail message.
         */
        public InvalidContentTypeException() {
            super();
        }

       
}
