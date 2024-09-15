@@ -1240,6 +1240,13 @@ public class NioEndpoint extends AbstractEndpoint {
                         sd.pos += written;
                         sd.length -= written;
                         attachment.access();
+                    } else {
+                        // Unusual not to be unable to transfer any bytes
+                        // Check the length was set correctly
+                        if (sd.fchannel.size() <= sd.pos) {
+                            throw new IOException("Sendfile configured to " +
+                                    "send more data than was available");
+                        }
                     }
                 }
                 if ( sd.length <= 0 && sc.getOutboundRemaining()<=0) {
