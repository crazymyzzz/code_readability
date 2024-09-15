@@ -55,8 +55,7 @@ public class TestCorsFilter {
         corsFilter.doFilter(request, response, filterChain);
 
         Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"));
         Assert.assertTrue(((Boolean) request.getAttribute(
                 CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST)).booleanValue());
         Assert.assertTrue(request.getAttribute(
@@ -88,8 +87,7 @@ public class TestCorsFilter {
         corsFilter.doFilter(request, response, filterChain);
 
         Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"));
         Assert.assertTrue(((Boolean) request.getAttribute(
                 CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST)).booleanValue());
         Assert.assertTrue(request.getAttribute(
@@ -120,8 +118,7 @@ public class TestCorsFilter {
         corsFilter.doFilter(request, response, filterChain);
 
         Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"));
         Assert.assertTrue(((Boolean) request.getAttribute(
                 CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST)).booleanValue());
         Assert.assertTrue(request.getAttribute(
@@ -166,41 +163,15 @@ public class TestCorsFilter {
     }
 
     /*
-     * Tests the presence of the origin (and not '*') in the response, when
-     * supports credentials is enabled alongwith any origin, '*'.
+     * Tests the that supports credentials may not be enabled with any origin,
+     * '*'.
      *
-     * @throws IOException
      * @throws ServletException
      */
-    @Test
-    public void testDoFilterSimpleAnyOriginAndSupportsCredentials()
-            throws IOException, ServletException {
-        TesterHttpServletRequest request = new TesterHttpServletRequest();
-        request.setHeader(CorsFilter.REQUEST_HEADER_ORIGIN,
-                TesterFilterConfigs.HTTPS_WWW_APACHE_ORG);
-        request.setMethod("GET");
-        TesterHttpServletResponse response = new TesterHttpServletResponse();
-
+    @Test(expected=ServletException.class)
+    public void testDoFilterSimpleAnyOriginAndSupportsCredentials() throws ServletException {
         CorsFilter corsFilter = new CorsFilter();
-        corsFilter.init(TesterFilterConfigs
-                .getFilterConfigAnyOriginAndSupportsCredentials());
-        corsFilter.doFilter(request, response, filterChain);
-
-        Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                TesterFilterConfigs.HTTPS_WWW_APACHE_ORG));
-        Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS)
-                .equals(
-                        "true"));
-        Assert.assertTrue(((Boolean) request.getAttribute(
-                CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST)).booleanValue());
-        Assert.assertTrue(request.getAttribute(
-                CorsFilter.HTTP_REQUEST_ATTRIBUTE_ORIGIN).equals(
-                TesterFilterConfigs.HTTPS_WWW_APACHE_ORG));
-        Assert.assertTrue(request.getAttribute(
-                CorsFilter.HTTP_REQUEST_ATTRIBUTE_REQUEST_TYPE).equals(
-                CorsFilter.CORSRequestType.SIMPLE.name().toLowerCase(Locale.ENGLISH)));
+        corsFilter.init(TesterFilterConfigs.getFilterConfigAnyOriginAndSupportsCredentials());
     }
 
     /*
@@ -261,8 +232,7 @@ public class TestCorsFilter {
         corsFilter.doFilter(request, response, filterChain);
 
         Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"));
         Assert.assertTrue(response.getHeader(
                 CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS)
                 .equals(TesterFilterConfigs.EXPOSED_HEADERS));
@@ -727,9 +697,8 @@ public class TestCorsFilter {
         });
         corsFilter.doFilter(request, response, filterChain);
 
-        Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+        Assert.assertNull(response.getHeader(
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN));
         Assert.assertTrue(((Boolean) request.getAttribute(
                 CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST)).booleanValue());
         Assert.assertTrue(request.getAttribute(
@@ -1412,7 +1381,7 @@ public class TestCorsFilter {
         Assert.assertTrue(corsFilter.getAllowedOrigins().size() == 0);
         Assert.assertTrue(corsFilter.isAnyOriginAllowed());
         Assert.assertTrue(corsFilter.getExposedHeaders().size() == 0);
-        Assert.assertTrue(corsFilter.isSupportsCredentials());
+        Assert.assertFalse(corsFilter.isSupportsCredentials());
         Assert.assertTrue(corsFilter.getPreflightMaxAge() == 1800);
     }
 
@@ -1448,9 +1417,9 @@ public class TestCorsFilter {
         Assert.assertTrue(corsFilter.getAllowedHttpHeaders().size() == 6);
         Assert.assertTrue(corsFilter.getAllowedHttpMethods().size() == 4);
         Assert.assertTrue(corsFilter.getAllowedOrigins().size() == 0);
-        Assert.assertTrue(corsFilter.isAnyOriginAllowed());
+        Assert.assertFalse(corsFilter.isAnyOriginAllowed());
         Assert.assertTrue(corsFilter.getExposedHeaders().size() == 0);
-        Assert.assertTrue(corsFilter.isSupportsCredentials());
+        Assert.assertFalse(corsFilter.isSupportsCredentials());
         Assert.assertTrue(corsFilter.getPreflightMaxAge() == 1800);
     }
 
@@ -1554,8 +1523,7 @@ public class TestCorsFilter {
         corsFilter.doFilter(request, response, filterChain);
 
         Assert.assertTrue(response.getHeader(
-                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals(
-                "https://www.apache.org"));
+                CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN).equals("*"));
         Assert.assertNull(request
                 .getAttribute(CorsFilter.HTTP_REQUEST_ATTRIBUTE_IS_CORS_REQUEST));
         Assert.assertNull(request
