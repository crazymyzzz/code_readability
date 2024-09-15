/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.tomcat.util.http.fileupload;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import org.apache.tomcat.util.http.fileupload.FileUploadBase.FileUploadIOException;
import org.apache.tomcat.util.http.fileupload.util.Closeable;
import org.apache.tomcat.util.http.fileupload.util.Streams;


/**
 * <p> Low level API for processing file uploads.
 *
 * <p> This class can be used to process data streams conforming to MIME
 * 'multipart' format as defined in
 * <a href="http://www.ietf.org/rfc/rfc1867.txt">RFC 1867</a>. Arbitrarily
 * large amounts of data in the stream can be processed under constant
 * memory usage.
 *
 * <p> The format of the stream is defined in the following way:<br>
 *
 * <code>
 *   multipart-body := preamble 1*encapsulation close-delimiter epilogue<br>
 *   encapsulation := delimiter body CRLF<br>
 *   delimiter := "--" boundary CRLF<br>
 *   close-delimiter := "--" boundary "--"<br>
 *   preamble := &lt;ignore&gt;<br>
 *   epilogue := &lt;ignore&gt;<br>
 *   body := header-part CRLF body-part<br>
 *   header-part := 1*header CRLF<br>
 *   header := header-name ":" header-value<br>
 *   header-name := &lt;printable ascii characters except ":"&gt;<br>
 *   header-value := &lt;any ascii characters except CR & LF&gt;<br>
 *   body-data := &lt;arbitrary data&gt;<br>
 * </code>
 *
 * <p>Note that body-data can contain another mulipart entity.  There
 * is limited support for single pass processing of such nested
 * streams.  The nested stream is <strong>required</strong> to have a
 * boundary token of the same length as the parent stream (see {@link
 * #setBoundary(byte[])}).
 *
 * <p>Here is an example of usage of this class.<br>
 *
 * <pre>
 *   try {
 *     MultipartStream multipartStream = new MultipartStream(input, boundary);
 *     boolean nextPart = multipartStream.skipPreamble();
 *     OutputStream output;
 *     while(nextPart) {
 *       String header = multipartStream.readHeaders();
 *       // process headers
 *       // create some output stream
 *       multipartStream.readBodyData(output);
 *       nextPart = multipartStream.readBoundary();
 *     }
 *   } catch(MultipartStream.MalformedStreamException e) {
 *     // the stream failed to follow required syntax
 *   } catch(IOException e) {
 *     // a read or write error occurred
 *   }
 * </pre>
 */
public class MultipartStream {



    /**
     * <p> Constructs a <code>MultipartStream</code> with a custom size buffer.
     *
     * <p> Note that the buffer must be at least big enough to contain the
     * boundary string, plus 4 characters for CR/LF and double dash, plus at
     * least one byte of data.  Too small a buffer size setting will degrade
     * performance.
     *
     * @param input    The <code>InputStream</code> to serve as a data source.
     * @param boundary The token used for dividing the stream into
     *                 <code>encapsulations</code>.
     * @param bufSize  The size of the buffer to be used, in bytes.
     * @param pNotifier The notifier, which is used for calling the
     *                  progress listener, if any.
     *
     * @see #MultipartStream(InputStream, byte[],
     *     MultipartStream.ProgressNotifier)
     */
    public MultipartStream(InputStream input,
            byte[] boundary,
            int bufSize,
            ProgressNotifier pNotifier) {
        this.input = input;
        this.bufSize = bufSize;
        this.buffer = new byte[bufSize];
        this.notifier = pNotifier;

        // We prepend CR/LF to the boundary to chop trailing CR/LF from
        // body-data tokens.
        this.boundary = new byte[boundary.length + BOUNDARY_PREFIX.length];
        this.boundaryLength = boundary.length + BOUNDARY_PREFIX.length;
        this.keepRegion = this.boundary.length;
        System.arraycopy(BOUNDARY_PREFIX, 0, this.boundary, 0,
                BOUNDARY_PREFIX.length);
        System.arraycopy(boundary, 0, this.boundary, BOUNDARY_PREFIX.length,
                boundary.length);

        head = 0;
        tail = 0;
    }

   

}
