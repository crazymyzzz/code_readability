@@ -1241,7 +1241,7 @@ public class NioEndpoint extends AbstractEndpoint {
                         sd.length -= written;
                         attachment.access();
                     } else {
-                        // Unusual not to be unable to transfer any bytes
+                        // Unusual not to be able to transfer any bytes
                         // Check the length was set correctly
                         if (sd.fchannel.size() <= sd.pos) {
                             throw new IOException("Sendfile configured to " +
