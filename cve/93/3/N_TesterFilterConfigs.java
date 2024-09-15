

public class TesterFilterConfigs {


    // Default config for the test is to allow any origin
    public static FilterConfig getDefaultFilterConfig() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = CorsFilter.DEFAULT_DECORATE_REQUEST;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }

    public static FilterConfig getFilterConfigAnyOriginAndSupportsCredentials() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials = "true";
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = CorsFilter.DEFAULT_DECORATE_REQUEST;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }

    public static FilterConfig
            getFilterConfigAnyOriginAndSupportsCredentialsDisabled() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS + ",PUT";
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials = "false";
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = CorsFilter.DEFAULT_DECORATE_REQUEST;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }



    public static FilterConfig getFilterConfigWithExposedHeaders() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = EXPOSED_HEADERS;
        final String supportCredentials =
                CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = CorsFilter.DEFAULT_DECORATE_REQUEST;

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }



    public static FilterConfig getFilterConfigDecorateRequestDisabled() {
        final String allowedHttpHeaders =
                CorsFilter.DEFAULT_ALLOWED_HTTP_HEADERS;
        final String allowedHttpMethods =
                CorsFilter.DEFAULT_ALLOWED_HTTP_METHODS;
        final String allowedOrigins = ANY_ORIGIN;
        final String exposedHeaders = CorsFilter.DEFAULT_EXPOSED_HEADERS;
        final String supportCredentials =
                CorsFilter.DEFAULT_SUPPORTS_CREDENTIALS;
        final String preflightMaxAge =
                CorsFilter.DEFAULT_PREFLIGHT_MAXAGE;
        final String decorateRequest = "false";

        return generateFilterConfig(allowedHttpHeaders, allowedHttpMethods,
                allowedOrigins, exposedHeaders, supportCredentials,
                preflightMaxAge, decorateRequest);
    }

 
}
