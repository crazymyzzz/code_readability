@@ -45,7 +45,7 @@ public class OAuth20CallbackAuthorizeEndpointController extends BaseOAuth20Contr
     @GetMapping(path = OAuth20Constants.BASE_OAUTH20_URL + '/' + OAuth20Constants.CALLBACK_AUTHORIZE_URL)
     public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) {
         val context = new JEEContext(request, response, getOAuthConfigurationContext().getSessionStore());
-        callback.perform(context, getOAuthConfigurationContext().getOauthConfig(), (object, ctx) -> false,
+        callback.perform(context, getOAuthConfigurationContext().getOauthConfig(), (object, ctx) -> Boolean.FALSE,
             context.getFullRequestURL(), Boolean.TRUE, Boolean.FALSE,
             Boolean.FALSE, Authenticators.CAS_OAUTH_CLIENT);
         var url = callback.getRedirectUrl();
