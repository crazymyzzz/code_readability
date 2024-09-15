@@ -256,17 +256,14 @@ public class CorsFilter extends GenericFilter {
 
         // Section 6.1.3
         // Add a single Access-Control-Allow-Origin header.
-        if (anyOriginAllowed && !supportsCredentials) {
-            // If resource doesn't support credentials and if any origin is
-            // allowed
-            // to make CORS request, return header with '*'.
+        if (anyOriginAllowed) {
+            // If any origin is allowed, return header with '*'.
             response.addHeader(
                     CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                     "*");
         } else {
-            // If the resource supports credentials add a single
-            // Access-Control-Allow-Origin header, with the value of the Origin
-            // header as value.
+            // Add a single Access-Control-Allow-Origin header, with the value
+            // of the Origin header as value.
             response.addHeader(
                     CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                     origin);
@@ -764,6 +761,10 @@ public class CorsFilter extends GenericFilter {
         // For any value other then 'true' this will be false.
         this.supportsCredentials = Boolean.parseBoolean(supportsCredentials);
 
+        if (this.supportsCredentials && this.anyOriginAllowed) {
+            throw new ServletException(sm.getString("corsFilter.invalidSupportsCredentials"));
+        }
+
         try {
             if (!preflightMaxAge.isEmpty()) {
                 this.preflightMaxAge = Long.parseLong(preflightMaxAge);

