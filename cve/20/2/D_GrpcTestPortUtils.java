
@@ -36,7 +36,7 @@ private static boolean isHttpsConfigured(SslServerConfig ssl) {
                 || !isDefaultProtocols(ssl.protocols);
     }
 
-    private static boolean isDefaultProtocols(List<String> protocols) {
+    private static boolean isDefaultProtocols(Set<String> protocols) {
         return protocols.size() == 2 && protocols.contains("TLSv1.3") && protocols.contains("TLSv1.2");
     }
 
