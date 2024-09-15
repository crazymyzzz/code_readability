@@ -60,12 +60,7 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
         this.request = request;
         headers = request.getMimeHeaders();
 
-        buf = new byte[headerBufferSize];
-//        if (headerBufferSize < (8 * 1024)) {
-//            bbuf = ByteBuffer.allocateDirect(6 * 1500);
-//        } else {
-//            bbuf = ByteBuffer.allocateDirect((headerBufferSize / 1500 + 1) * 1500);
-//        }
+        this.headerBufferSize = headerBufferSize;
 
         inputStreamInputBuffer = new SocketInputBuffer();
 

 
@@ -115,6 +132,12 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
      */
     public void setSocket(NioChannel socket) {
         this.socket = socket;
+        socketReadBufferSize = socket.getBufHandler().getReadBuffer().capacity();
+        int bufLength = skipBlankLinesSize + headerBufferSize
+                + socketReadBufferSize;
+        if (buf == null || buf.length < bufLength) {
+            buf = new byte[bufLength];
+        }
     }
     
     
@@ -213,25 +236,23 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
                     if (useAvailableDataOnly) {
                         return false;
                     }
+                    // Ignore bytes that were read
+                    pos = lastValid = 0;
                     // Do a simple read with a short timeout
                     if ( readSocket(true, false)==0 ) return false;
                 }
                 chr = buf[pos++];
             } while ((chr == Constants.CR) || (chr == Constants.LF));
             pos--;
-            parsingRequestLineStart = pos;
-            parsingRequestLinePhase = 1;
-        } 
-        if ( parsingRequestLinePhase == 1 ) {
-            // Mark the current buffer position
-            
-            if (pos >= lastValid) {
-                if (useAvailableDataOnly) {
-                    return false;
-                }
-                // Do a simple read with a short timeout
-                if ( readSocket(true, false)==0 ) return false;
+            if (pos >= skipBlankLinesSize) {
+                // Move data, to have enough space for further reading
+                // of headers and body
+                System.arraycopy(buf, pos, buf, 0, lastValid - pos);
+                lastValid -= pos;
+                pos = 0;
             }
+            skipBlankLinesBytes = pos;
+            parsingRequestLineStart = pos;
             parsingRequestLinePhase = 2;
             if (log.isDebugEnabled()) {
                 log.debug("Received ["
@@ -380,6 +401,13 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
     
     private void expand(int newsize) {
         if ( newsize > buf.length ) {
+            if (parsingHeader) {
+                throw new IllegalArgumentException(
+                        sm.getString("iib.requestheadertoolarge.error"));
+            }
+            // Should not happen
+            log.warn("Expanding buffer size. Old size: " + buf.length
+                    + ", new size: " + newsize, new Exception());
             byte[] tmp = new byte[newsize];
             System.arraycopy(buf,0,tmp,0,buf.length);
             buf = tmp;
@@ -446,6 +474,19 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
         if (status == HeaderParseStatus.DONE) {
             parsingHeader = false;
             end = pos;
+            // Checking that
+            // (1) Headers plus request line size does not exceed its limit
+            // (2) There are enough bytes to avoid expanding the buffer when
+            // reading body
+            // Technically, (2) is technical limitation, (1) is logical
+            // limitation to enforce the meaning of headerBufferSize
+            // From the way how buf is allocated and how blank lines are being
+            // read, it should be enough to check (1) only.
+            if (end - skipBlankLinesBytes > headerBufferSize
+                    || buf.length - end < socketReadBufferSize) {
+                throw new IllegalArgumentException(
+                        sm.getString("iib.requestheadertoolarge.error"));
+            }
             return true;
         } else {
             return false;
@@ -684,16 +725,7 @@ public class InternalNioInputBuffer extends AbstractInputBuffer {
             // Do a simple read with a short timeout
             read = readSocket(timeout,block)>0;
         } else {
-
-            if (buf.length - end < 4500) {
-                // In this case, the request header was really large, so we allocate a 
-                // brand new one; the old one will get GCed when subsequent requests
-                // clear all references
-                buf = new byte[buf.length];
-                end = 0;
-            }
-            pos = end;
-            lastValid = pos;
+            lastValid = pos = end;
             // Do a simple read with a short timeout
             read = readSocket(timeout, block)>0;
         }
