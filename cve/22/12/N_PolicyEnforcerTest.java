

/**
 * @author <a href="mailto:psilva@redhat.com">Pedro Igor</a>
 */
@QuarkusTest
@QuarkusTestResource(KeycloakLifecycleManager.class)
public class PolicyEnforcerTest {
    

    private void testWebAppTenantAllowed(String user) throws Exception {
        try (final org.htmlunit.WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/api-permission-webapp");

            assertEquals("Sign in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute(user);
            loginForm.getInputByName("password").setValueAttribute(user);
            WebResponse response = loginForm.getInputByName("login").click().getWebResponse();
            assertEquals(200, response.getStatusCode());
            assertTrue(response.getContentAsString().contains("Permission Resource WebApp"));

            // Token is encrypted in the cookie
            Cookie cookie = webClient.getCookieManager().getCookie("q_session_api-permission-webapp");
            assureGetPathWithCookie("/api-permission-webapp", cookie, 200, null, "Permission Resource WebApp");
            assureGetPathWithCookie("//api-permission-webapp", cookie, 200, null, "Permission Resource WebApp");

            webClient.getCookieManager().clearCookies();
        }
    }

    private void testWebAppTenantForbidden(String user) throws Exception {
        try (final org.htmlunit.WebClient webClient = createWebClient()) {
            HtmlPage page = webClient.getPage("http://localhost:8081/api-permission-webapp");

            assertEquals("Sign in to quarkus", page.getTitleText());

            HtmlForm loginForm = page.getForms().get(0);

            loginForm.getInputByName("username").setValueAttribute(user);
            loginForm.getInputByName("password").setValueAttribute(user);
            try {
                loginForm.getInputByName("login").click();
                fail("403 status error is expected");
            } catch (FailingHttpStatusCodeException ex) {
                assertEquals(403, ex.getStatusCode());
            }

            // Token is encrypted in the cookie
            Cookie cookie = webClient.getCookieManager().getCookie("q_session_api-permission-webapp");
            assureGetPathWithCookie("/api-permission-webapp", cookie, 403, null, null);
            assureGetPathWithCookie("//api-permission-webapp", cookie, 403, null, null);

            webClient.getCookieManager().clearCookies();
        }
    }

    private org.htmlunit.WebClient createWebClient() {
        org.htmlunit.WebClient webClient = new org.htmlunit.WebClient();
        webClient.setCssErrorHandler(new SilentCssErrorHandler());
        return webClient;
    }

    

}
