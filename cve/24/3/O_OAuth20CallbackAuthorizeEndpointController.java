

/**
 * OAuth callback authorize controller based on the pac4j callback controller.
 *
 * @author Jerome Leleu
 * @since 3.5.0
 */
@Slf4j
public class OAuth20CallbackAuthorizeEndpointController extends BaseOAuth20Controller {
    
    /**
     * Handle request.
     *
     * @param request  the request
     * @param response the response
     * @return the model and view
     */
    @GetMapping(path = OAuth20Constants.BASE_OAUTH20_URL + '/' + OAuth20Constants.CALLBACK_AUTHORIZE_URL)
    public ModelAndView handleRequest(final HttpServletRequest request, final HttpServletResponse response) {
        val context = new JEEContext(request, response, getOAuthConfigurationContext().getSessionStore());
        callback.perform(context, getOAuthConfigurationContext().getOauthConfig(), (object, ctx) -> false,
            context.getFullRequestURL(), Boolean.TRUE, Boolean.FALSE,
            Boolean.FALSE, Authenticators.CAS_OAUTH_CLIENT);
        var url = callback.getRedirectUrl();
        if (StringUtils.isBlank(url)) {
            val msg = "Unable to locate OAuth redirect URL from the session store; Stale or mismatched session request?";
            LOGGER.error(msg);
            return WebUtils.produceErrorView(OAuth20Constants.SESSION_STALE_MISMATCH, new OAuth20SessionStoreMismatchException(msg));
        }
        val manager = new ProfileManager<>(context, context.getSessionStore());
        LOGGER.trace("OAuth callback URL is [{}]", url);
        return getOAuthConfigurationContext().getCallbackAuthorizeViewResolver().resolve(context, manager, url);
    }


}
