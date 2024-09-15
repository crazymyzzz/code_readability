
@@ -173,8 +261,13 @@ public class DigestAuthenticator
 
         // Validate any credentials already included with this request
         String authorization = request.getHeader("authorization");
+        DigestInfo digestInfo = new DigestInfo(getOpaque(), getNonceValidity(),
+                getKey(), cnonces, isValidateUri());
         if (authorization != null) {
-            principal = findPrincipal(request, authorization, context.getRealm());
+            if (digestInfo.validate(request, authorization, config)) {
+                principal = digestInfo.authenticate(context.getRealm());
+            }
+            
             if (principal != null) {
                 String username = parseUsername(authorization);
                 register(request, response, principal,
@@ -188,9 +281,10 @@ public class DigestAuthenticator
 
         // Next, generate a nOnce token (that is a token which is supposed
         // to be unique).
-        String nOnce = generateNOnce(request);
+        String nonce = generateNonce(request);
 
-        setAuthenticateHeader(request, response, config, nOnce);
+        setAuthenticateHeader(request, response, config, nonce,
+                digestInfo.isNonceStale());
         response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
         //      hres.flushBuffer();
         return (false);

@@ -301,7 +309,6 @@ public class DigestAuthenticator
      */
     protected String parseUsername(String authorization) {
 
-        //System.out.println("Authorization token : " + authorization);
         // Validate the authorization credentials format
         if (authorization == null)
             return (null);
@@ -361,20 +368,20 @@ public class DigestAuthenticator
      *
      * @param request HTTP Servlet request
      */
-    protected String generateNOnce(Request request) {
+    protected String generateNonce(Request request) {
 
         long currentTime = System.currentTimeMillis();
 
-        String nOnceValue = request.getRemoteAddr() + ":" +
-            currentTime + ":" + key;
+        
+        String ipTimeKey =
+            request.getRemoteAddr() + ":" + currentTime + ":" + getKey();
 
-        byte[] buffer = null;
+        byte[] buffer;
         synchronized (md5Helper) {
-            buffer = md5Helper.digest(nOnceValue.getBytes());
+            buffer = md5Helper.digest(ipTimeKey.getBytes());
         }
-        nOnceValue = md5Encoder.encode(buffer);
 
-        return nOnceValue;
+        return currentTime + ":" + md5Encoder.encode(buffer);
     }
 
 
@@ -408,24 +415,298 @@ public class DigestAuthenticator
     protected void setAuthenticateHeader(HttpServletRequest request,
                                          HttpServletResponse response,
                                          LoginConfig config,
-                                         String nOnce) {
+                                         String nOnce,
+                                         boolean isNonceStale) {
 
         // Get the realm name
         String realmName = config.getRealmName();
         if (realmName == null)
             realmName = REALM_NAME;
 
-        byte[] buffer = null;
-        synchronized (md5Helper) {
-            buffer = md5Helper.digest(nOnce.getBytes());
+        String authenticateHeader;
+        if (isNonceStale) {
+            authenticateHeader = "Digest realm=\"" + realmName + "\", " +
+            "qop=\"" + QOP + "\", nonce=\"" + nOnce + "\", " + "opaque=\"" +
+            getOpaque() + "\", stale=true";
+        } else {
+            authenticateHeader = "Digest realm=\"" + realmName + "\", " +
+            "qop=\"" + QOP + "\", nonce=\"" + nOnce + "\", " + "opaque=\"" +
+            getOpaque() + "\"";
         }
 
-        String authenticateHeader = "Digest realm=\"" + realmName + "\", "
-            +  "qop=\"auth\", nonce=\"" + nOnce + "\", " + "opaque=\""
-            + md5Encoder.encode(buffer) + "\"";
         response.setHeader(AUTH_HEADER_NAME, authenticateHeader);
 
     }
 
 

 }
