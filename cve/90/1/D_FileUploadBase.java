@@ -799,7 +799,7 @@ public abstract class FileUploadBase {
                     || (!contentType.toLowerCase(Locale.ENGLISH).startsWith(MULTIPART))) {
                 throw new InvalidContentTypeException(String.format(
                         "the request doesn't contain a %s or %s stream, content type header is %s",
-                        MULTIPART_FORM_DATA, MULTIPART_FORM_DATA, contentType));
+                        MULTIPART_FORM_DATA, MULTIPART_MIXED, contentType));
             }
 
             InputStream input = ctx.getInputStream();
@@ -810,8 +810,7 @@ public abstract class FileUploadBase {
                 if (requestSize != -1 && requestSize > sizeMax) {
                     throw new SizeLimitExceededException(String.format(
                             "the request was rejected because its size (%s) exceeds the configured maximum (%s)",
-                            Long.valueOf(requestSize),
-                            Long.valueOf(sizeMax)),
+                            Long.valueOf(requestSize), Long.valueOf(sizeMax)),
                             requestSize, sizeMax);
                 }
                 input = new LimitedInputStream(input, sizeMax) {
@@ -838,7 +837,13 @@ public abstract class FileUploadBase {
             }
 
             notifier = new MultipartStream.ProgressNotifier(listener, requestSize);
-            multi = new MultipartStream(input, boundary, notifier);
+            try {
+                multi = new MultipartStream(input, boundary, notifier);
+            } catch (IllegalArgumentException iae) {
+                throw new InvalidContentTypeException(String.format(
+                        "The boundary specified in the %s header is too long",
+                        CONTENT_TYPE), iae);
+            }
             multi.setHeaderEncoding(charEncoding);
 
             skipPreamble = true;
@@ -1016,7 +1021,7 @@ public abstract class FileUploadBase {
          * detail message.
          */
         public InvalidContentTypeException() {
-            // Nothing to do.
+            super();
         }
 
         /**

