

/**
* Servlet that enables remote management of the web applications deployed
* within the same virtual host as this web application is.  Normally, this
* functionality will be protected by a security constraint in the web
* application deployment descriptor.  However, this requirement can be
* relaxed during testing.
* <p>
* The difference between the <code>ManagerServlet</code> and this
* Servlet is that this Servlet prints out a HTML interface which
* makes it easier to administrate.
* <p>
* However if you use a software that parses the output of
* <code>ManagerServlet</code> you won't be able to upgrade
* to this Servlet since the output are not in the
* same format ar from <code>ManagerServlet</code>
*
* @author Bip Thelin
* @author Malcolm Edgar
* @author Glenn L. Nielsen
* @version $Revision$, $Date$
* @see ManagerServlet
*/

public final class HTMLManagerServlet extends ManagerServlet {


    // --------------------------------------------------------- Public Methods

    /**
     * Process a GET request for the specified resource.
     *
     * @param request The servlet request we are processing
     * @param response The servlet response we are creating
     *
     * @exception IOException if an input/output error occurs
     * @exception ServletException if a servlet-specified error occurs
     */
    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
        throws IOException, ServletException {

        // Identify the request parameters that we need
        String command = request.getPathInfo();

        String path = request.getParameter("path");
        String deployPath = request.getParameter("deployPath");
        String deployConfig = request.getParameter("deployConfig");
        String deployWar = request.getParameter("deployWar");

        // Prepare our output writer to generate the response message
        response.setContentType("text/html; charset=" + Constants.CHARSET);

        String message = "";
        // Process the requested command
        if (command == null || command.equals("/")) {
        } else if (command.equals("/deploy")) {
            message = deployInternal(deployConfig, deployPath, deployWar);
        } else if (command.equals("/list")) {
        } else if (command.equals("/reload")) {
            message = reload(path);
        } else if (command.equals("/undeploy")) {
            message = undeploy(path);
        } else if (command.equals("/expire")) {
            message = expireSessions(path, request);
        } else if (command.equals("/sessions")) {
            try {
                doSessions(path, request, response);
                return;
            } catch (Exception e) {
                log("HTMLManagerServlet.sessions[" + path + "]", e);
                message = sm.getString("managerServlet.exception",
                        e.toString());
            }
        } else if (command.equals("/start")) {
            message = start(path);
        } else if (command.equals("/stop")) {
            message = stop(path);
        } else {
            message =
                sm.getString("managerServlet.unknownCommand", command);
        }

        list(request, response, message);
    }



    /**
     * Render a HTML list of the currently active Contexts in our virtual host,
     * and memory and server status information.
     *
     * @param request The request
     * @param response The response
     * @param message a message to display
     */
    public void list(HttpServletRequest request,
                     HttpServletResponse response,
                     String message) throws IOException {

        if (debug >= 1)
            log("list: Listing contexts for virtual host '" +
                host.getName() + "'");

        PrintWriter writer = response.getWriter();

        // HTML Header Section
        writer.print(Constants.HTML_HEADER_SECTION);

        // Body Header Section
        Object[] args = new Object[2];
        args[0] = request.getContextPath();
        args[1] = sm.getString("htmlManagerServlet.title");
        writer.print(MessageFormat.format
                     (Constants.BODY_HEADER_SECTION, args));

        // Message Section
        args = new Object[3];
        args[0] = sm.getString("htmlManagerServlet.messageLabel");
        if (message == null || message.length() == 0) {
            args[1] = "OK";
        } else {
            args[1] = RequestUtil.filter(message);
        }
        writer.print(MessageFormat.format(Constants.MESSAGE_SECTION, args));

        // Manager Section
        args = new Object[9];
        args[0] = sm.getString("htmlManagerServlet.manager");
        args[1] = response.encodeURL(request.getContextPath() + "/html/list");
        args[2] = sm.getString("htmlManagerServlet.list");
        args[3] = response.encodeURL
            (request.getContextPath() + "/" +
             sm.getString("htmlManagerServlet.helpHtmlManagerFile"));
        args[4] = sm.getString("htmlManagerServlet.helpHtmlManager");
        args[5] = response.encodeURL
            (request.getContextPath() + "/" +
             sm.getString("htmlManagerServlet.helpManagerFile"));
        args[6] = sm.getString("htmlManagerServlet.helpManager");
        args[7] = response.encodeURL
            (request.getContextPath() + "/status");
        args[8] = sm.getString("statusServlet.title");
        writer.print(MessageFormat.format(Constants.MANAGER_SECTION, args));

        // Apps Header Section
        args = new Object[6];
        args[0] = sm.getString("htmlManagerServlet.appsTitle");
        args[1] = sm.getString("htmlManagerServlet.appsPath");
        args[2] = sm.getString("htmlManagerServlet.appsName");
        args[3] = sm.getString("htmlManagerServlet.appsAvailable");
        args[4] = sm.getString("htmlManagerServlet.appsSessions");
        args[5] = sm.getString("htmlManagerServlet.appsTasks");
        writer.print(MessageFormat.format(APPS_HEADER_SECTION, args));

        // Apps Row Section
        // Create sorted map of deployed applications context paths.
        Container children[] = host.findChildren();
        String contextPaths[] = new String[children.length];
        for (int i = 0; i < children.length; i++)
            contextPaths[i] = children[i].getName();

        TreeMap sortedContextPathsMap = new TreeMap();

        for (int i = 0; i < contextPaths.length; i++) {
            String displayPath = contextPaths[i];
            sortedContextPathsMap.put(displayPath, contextPaths[i]);
        }

        String appsStart = sm.getString("htmlManagerServlet.appsStart");
        String appsStop = sm.getString("htmlManagerServlet.appsStop");
        String appsReload = sm.getString("htmlManagerServlet.appsReload");
        String appsUndeploy = sm.getString("htmlManagerServlet.appsUndeploy");
        String appsExpire = sm.getString("htmlManagerServlet.appsExpire");

        Iterator iterator = sortedContextPathsMap.entrySet().iterator();
        boolean isHighlighted = true;
        boolean isDeployed = true;
        String highlightColor = null;

        while (iterator.hasNext()) {
            // Bugzilla 34818, alternating row colors
            isHighlighted = !isHighlighted;
            if(isHighlighted) {
                highlightColor = "#C3F3C3";
            } else {
                highlightColor = "#FFFFFF";
            }

            Map.Entry entry = (Map.Entry) iterator.next();
            String displayPath = (String) entry.getKey();
            String contextPath = (String) entry.getKey();
            Context context = (Context) host.findChild(contextPath);
            if (displayPath.equals("")) {
                displayPath = "/";
            }

            if (context != null ) {
                try {
                    isDeployed = isDeployed(contextPath);
                } catch (Exception e) {
                    // Assume false on failure for safety
                    isDeployed = false;
                }
                
                args = new Object[6];
                args[0] = displayPath;
                args[1] = context.getDisplayName();
                if (args[1] == null) {
                    args[1] = "&nbsp;";
                }
                args[2] = new Boolean(context.getAvailable());
                args[3] = response.encodeURL
                    (request.getContextPath() +
                     "/html/sessions?path=" + displayPath);
                if (context.getManager() != null) {
                    args[4] = new Integer
                        (context.getManager().getActiveSessions());
                } else {
                    args[4] = new Integer(0);
                }

                args[5] = highlightColor;

                writer.print
                    (MessageFormat.format(APPS_ROW_DETAILS_SECTION, args));

                args = new Object[14];
                args[0] = response.encodeURL
                    (request.getContextPath() +
                     "/html/start?path=" + displayPath);
                args[1] = appsStart;
                args[2] = response.encodeURL
                    (request.getContextPath() +
                     "/html/stop?path=" + displayPath);
                args[3] = appsStop;
                args[4] = response.encodeURL
                    (request.getContextPath() +
                     "/html/reload?path=" + displayPath);
                args[5] = appsReload;
                args[6] = response.encodeURL
                    (request.getContextPath() +
                     "/html/undeploy?path=" + displayPath);
                args[7] = appsUndeploy;
                
                args[8] = response.encodeURL
                    (request.getContextPath() +
                     "/html/expire?path=" + displayPath);
                args[9] = appsExpire;
                args[10] = sm.getString("htmlManagerServlet.expire.explain");
                args[11] = new Integer(context.getManager().getMaxInactiveInterval()/60);
                args[12] = sm.getString("htmlManagerServlet.expire.unit");
                
                args[13] = highlightColor;

                if (context.getPath().equals(this.context.getPath())) {
                    writer.print(MessageFormat.format(
                        MANAGER_APP_ROW_BUTTON_SECTION, args));
                } else if (context.getAvailable() && isDeployed) {
                    writer.print(MessageFormat.format(
                        STARTED_DEPLOYED_APPS_ROW_BUTTON_SECTION, args));
                } else if (context.getAvailable() && !isDeployed) {
                    writer.print(MessageFormat.format(
                        STARTED_NONDEPLOYED_APPS_ROW_BUTTON_SECTION, args));
                } else if (!context.getAvailable() && isDeployed) {
                    writer.print(MessageFormat.format(
                        STOPPED_DEPLOYED_APPS_ROW_BUTTON_SECTION, args));
                } else {
                    writer.print(MessageFormat.format(
                        STOPPED_NONDEPLOYED_APPS_ROW_BUTTON_SECTION, args));
                }

            }
        }

        // Deploy Section
        args = new Object[7];
        args[0] = sm.getString("htmlManagerServlet.deployTitle");
        args[1] = sm.getString("htmlManagerServlet.deployServer");
        args[2] = response.encodeURL(request.getContextPath() + "/html/deploy");
        args[3] = sm.getString("htmlManagerServlet.deployPath");
        args[4] = sm.getString("htmlManagerServlet.deployConfig");
        args[5] = sm.getString("htmlManagerServlet.deployWar");
        args[6] = sm.getString("htmlManagerServlet.deployButton");
        writer.print(MessageFormat.format(DEPLOY_SECTION, args));

        args = new Object[4];
        args[0] = sm.getString("htmlManagerServlet.deployUpload");
        args[1] = response.encodeURL(request.getContextPath() + "/html/upload");
        args[2] = sm.getString("htmlManagerServlet.deployUploadFile");
        args[3] = sm.getString("htmlManagerServlet.deployButton");
        writer.print(MessageFormat.format(UPLOAD_SECTION, args));

        // Server Header Section
        args = new Object[7];
        args[0] = sm.getString("htmlManagerServlet.serverTitle");
        args[1] = sm.getString("htmlManagerServlet.serverVersion");
        args[2] = sm.getString("htmlManagerServlet.serverJVMVersion");
        args[3] = sm.getString("htmlManagerServlet.serverJVMVendor");
        args[4] = sm.getString("htmlManagerServlet.serverOSName");
        args[5] = sm.getString("htmlManagerServlet.serverOSVersion");
        args[6] = sm.getString("htmlManagerServlet.serverOSArch");
        writer.print(MessageFormat.format
                     (Constants.SERVER_HEADER_SECTION, args));

        // Server Row Section
        args = new Object[6];
        args[0] = ServerInfo.getServerInfo();
        args[1] = System.getProperty("java.runtime.version");
        args[2] = System.getProperty("java.vm.vendor");
        args[3] = System.getProperty("os.name");
        args[4] = System.getProperty("os.version");
        args[5] = System.getProperty("os.arch");
        writer.print(MessageFormat.format(Constants.SERVER_ROW_SECTION, args));

        // HTML Tail Section
        writer.print(Constants.HTML_TAIL_SECTION);

        // Finish up the response
        writer.flush();
        writer.close();
    }

    
}
