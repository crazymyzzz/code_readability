
@@ -74,7 +73,7 @@ public void testUserHasSuperUserRoleWebTenant() throws Exception {
     }
 
     private void testWebAppTenantAllowed(String user) throws Exception {
-        try (final com.gargoylesoftware.htmlunit.WebClient webClient = createWebClient()) {
+        try (final org.htmlunit.WebClient webClient = createWebClient()) {
             HtmlPage page = webClient.getPage("http://localhost:8081/api-permission-webapp");
 
             assertEquals("Sign in to quarkus", page.getTitleText());
@@ -97,7 +96,7 @@ private void testWebAppTenantAllowed(String user) throws Exception {
     }
 
     private void testWebAppTenantForbidden(String user) throws Exception {
-        try (final com.gargoylesoftware.htmlunit.WebClient webClient = createWebClient()) {
+        try (final org.htmlunit.WebClient webClient = createWebClient()) {
             HtmlPage page = webClient.getPage("http://localhost:8081/api-permission-webapp");
 
             assertEquals("Sign in to quarkus", page.getTitleText());
@@ -122,8 +121,8 @@ private void testWebAppTenantForbidden(String user) throws Exception {
         }
     }
 
-    private com.gargoylesoftware.htmlunit.WebClient createWebClient() {
-        com.gargoylesoftware.htmlunit.WebClient webClient = new com.gargoylesoftware.htmlunit.WebClient();
+    private org.htmlunit.WebClient createWebClient() {
+        org.htmlunit.WebClient webClient = new org.htmlunit.WebClient();
         webClient.setCssErrorHandler(new SilentCssErrorHandler());
         return webClient;
     }
