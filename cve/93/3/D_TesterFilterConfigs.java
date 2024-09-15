@@ -36,12 +36,13 @@ public class TesterFilterConfigs {
     public static final TesterServletContext mockServletContext =
             new TesterServletContext();
 
+    // Default config for the test is to allow any origin
     public static FilterConfig getDefaultFilterConfig() {
         final String allowedHttpHeaders =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
         final String allowedHttpMethods =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
-        final String allowedOrigins = CorsFilter.DEFAULT_ALLOWED_ORIGINS;
+        final String allowedOrigins = ANY_ORIGIN;
         final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
         final String supportCredentials =
                 CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
@@ -59,7 +60,7 @@ public class TesterFilterConfigs {
                 CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
         final String allowedHttpMethods =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
-        final String allowedOrigins = CorsFilter.DEFAULT_ALLOWED_ORIGINS;
+        final String allowedOrigins = ANY_ORIGIN;
         final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
         final String supportCredentials = "true";
         final String preflightMaxAge =
@@ -77,7 +78,7 @@ public class TesterFilterConfigs {
                 CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
         final String allowedHttpMethods =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
-        final String allowedOrigins = CorsFilter.DEFAULT_ALLOWED_ORIGINS;
+        final String allowedOrigins = ANY_ORIGIN;
         final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
         final String supportCredentials = "false";
         final String preflightMaxAge =
@@ -131,7 +132,7 @@ public class TesterFilterConfigs {
                 CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
         final String allowedHttpMethods =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
-        final String allowedOrigins = CorsFilter.DEFAULT_ALLOWED_ORIGINS;
+        final String allowedOrigins = ANY_ORIGIN;
         final String exposedHeaders = EXPOSED_HEADERS;
         final String supportCredentials =
                 CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
@@ -240,7 +241,7 @@ public class TesterFilterConfigs {
                 CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
         final String allowedHttpMethods =
                 CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
-        final String allowedOrigins = CorsFilter.DEFAULT_ALLOWED_ORIGINS;
+        final String allowedOrigins = ANY_ORIGIN;
         final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
         final String supportCredentials =
                 CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
