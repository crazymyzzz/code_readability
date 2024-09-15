

/**
 * <p>
 * A {@link javax.servlet.Filter} that enable client-side cross-origin requests
 * by implementing W3C's CORS (<b>C</b>ross-<b>O</b>rigin <b>R</b>esource
 * <b>S</b>haring) specification for resources. Each {@link HttpServletRequest}
 * request is inspected as per specification, and appropriate response headers
 * are added to {@link HttpServletResponse}.
 * </p>
 *
 * <p>
 * By default, it also sets following request attributes, that help to
 * determine the nature of the request downstream.
 * </p>
 * <ul>
 * <li><b>cors.isCorsRequest:</b> Flag to determine if the request is a CORS
 * request. Set to <code>true</code> if a CORS request; <code>false</code>
 * otherwise.</li>
 * <li><b>cors.request.origin:</b> The Origin URL, i.e. the URL of the page from
 * where the request is originated.</li>
 * <li>
 * <b>cors.request.type:</b> Type of request. Possible values:
 * <ul>
 * <li>SIMPLE: A request which is not preceded by a pre-flight request.</li>
 * <li>ACTUAL: A request which is preceded by a pre-flight request.</li>
 * <li>PRE_FLIGHT: A pre-flight request.</li>
 * <li>NOT_CORS: A normal same-origin request.</li>
 * <li>INVALID_CORS: A cross-origin request which is invalid.</li>
 * </ul>
 * </li>
 * <li><b>cors.request.headers:</b> Request headers sent as
 * 'Access-Control-Request-Headers' header, for pre-flight request.</li>
 * </ul>
 *
 * @see <a href="http://www.w3.org/TR/cors/">CORS specification</a>
 *
 */
public class CorsFilter extends GenericFilter {




    /**
     * Handles a CORS request of type {@link CORSRequestType}.SIMPLE.
     *
     * @param request The {@link HttpServletRequest} object.
     * @param response The {@link HttpServletResponse} object.
     * @param filterChain The {@link FilterChain} object.
     * @throws IOException an IO error occurred
     * @throws ServletException Servlet error propagation
     * @see <a href="http://www.w3.org/TR/cors/#resource-requests">Simple
     *      Cross-Origin Request, Actual Request, and Redirects</a>
     */
    protected void handleSimpleCORS(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain filterChain)
            throws IOException, ServletException {

        CorsFilter.CORSRequestType requestType = checkRequestType(request);
        if (!(requestType == CorsFilter.CORSRequestType.SIMPLE ||
                requestType == CorsFilter.CORSRequestType.ACTUAL)) {
            throw new IllegalArgumentException(
                    sm.getString("corsFilter.wrongType2",
                            CorsFilter.CORSRequestType.SIMPLE,
                            CorsFilter.CORSRequestType.ACTUAL));
        }

        final String origin = request
                .getHeader(CorsFilter.REQUEST_HEADER_ORIGIN);
        final String method = request.getMethod();

        // Section 6.1.2
        if (!isOriginAllowed(origin)) {
            handleInvalidCORS(request, response, filterChain);
            return;
        }

        if (!allowedHttpMethods.contains(method)) {
            handleInvalidCORS(request, response, filterChain);
            return;
        }

        // Section 6.1.3
        // Add a single Access-Control-Allow-Origin header.
        if (anyOriginAllowed && !supportsCredentials) {
            // If resource doesn't support credentials and if any origin is
            // allowed
            // to make CORS request, return header with '*'.
            response.addHeader(
                    CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                    "*");
        } else {
            // If the resource supports credentials add a single
            // Access-Control-Allow-Origin header, with the value of the Origin
            // header as value.
            response.addHeader(
                    CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_ORIGIN,
                    origin);
        }

        // Section 6.1.3
        // If the resource supports credentials, add a single
        // Access-Control-Allow-Credentials header with the case-sensitive
        // string "true" as value.
        if (supportsCredentials) {
            response.addHeader(
                    CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_ALLOW_CREDENTIALS,
                    "true");
        }

        // Section 6.1.4
        // If the list of exposed headers is not empty add one or more
        // Access-Control-Expose-Headers headers, with as values the header
        // field names given in the list of exposed headers.
        if ((exposedHeaders != null) && (exposedHeaders.size() > 0)) {
            String exposedHeadersString = join(exposedHeaders, ",");
            response.addHeader(
                    CorsFilter.RESPONSE_HEADER_ACCESS_CONTROL_EXPOSE_HEADERS,
                    exposedHeadersString);
        }

        // Indicate the response depends on the origin
        response.addHeader(CorsFilter.REQUEST_HEADER_VARY,
                CorsFilter.REQUEST_HEADER_ORIGIN);

        // Forward the request down the filter chain.
        filterChain.doFilter(request, response);
    }



    /**
     * Parses each param-value and populates configuration variables. If a param
     * is provided, it overrides the default.
     *
     * @param allowedOrigins
     *            A {@link String} of comma separated origins.
     * @param allowedHttpMethods
     *            A {@link String} of comma separated HTTP methods.
     * @param allowedHttpHeaders
     *            A {@link String} of comma separated HTTP headers.
     * @param exposedHeaders
     *            A {@link String} of comma separated headers that needs to be
     *            exposed.
     * @param supportsCredentials
     *            "true" if support credentials needs to be enabled.
     * @param preflightMaxAge
     *            The amount of seconds the user agent is allowed to cache the
     *            result of the pre-flight request.
     * @throws ServletException
     */
    private void parseAndStore(final String allowedOrigins,
            final String allowedHttpMethods, final String allowedHttpHeaders,
            final String exposedHeaders, final String supportsCredentials,
            final String preflightMaxAge, final String decorateRequest)
                    throws ServletException {

        if (allowedOrigins.trim().equals("*")) {
            this.anyOriginAllowed = true;
        } else {
            this.anyOriginAllowed = false;
            Set<String> setAllowedOrigins = parseStringToSet(allowedOrigins);
            this.allowedOrigins.clear();
            this.allowedOrigins.addAll(setAllowedOrigins);
        }

        Set<String> setAllowedHttpMethods = parseStringToSet(allowedHttpMethods);
        this.allowedHttpMethods.clear();
        this.allowedHttpMethods.addAll(setAllowedHttpMethods);

        Set<String> setAllowedHttpHeaders = parseStringToSet(allowedHttpHeaders);
        Set<String> lowerCaseHeaders = new HashSet<>();
        for (String header : setAllowedHttpHeaders) {
            String lowerCase = header.toLowerCase(Locale.ENGLISH);
            lowerCaseHeaders.add(lowerCase);
        }
        this.allowedHttpHeaders.clear();
        this.allowedHttpHeaders.addAll(lowerCaseHeaders);

        Set<String> setExposedHeaders = parseStringToSet(exposedHeaders);
        this.exposedHeaders.clear();
        this.exposedHeaders.addAll(setExposedHeaders);

        // For any value other then 'true' this will be false.
        this.supportsCredentials = Boolean.parseBoolean(supportsCredentials);

        try {
            if (!preflightMaxAge.isEmpty()) {
                this.preflightMaxAge = Long.parseLong(preflightMaxAge);
            } else {
                this.preflightMaxAge = 0L;
            }
        } catch (NumberFormatException e) {
            throw new ServletException(
                    sm.getString("corsFilter.invalidPreflightMaxAge"), e);
        }

        // For any value other then 'true' this will be false.
        this.decorateRequest = Boolean.parseBoolean(decorateRequest);
    }

   
}
