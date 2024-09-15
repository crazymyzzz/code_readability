



/**
 * Servlet which adds support for WebDAV level 2. All the basic HTTP requests
 * are handled by the DefaultServlet. The WebDAVServlet must not be used as the
 * default servlet (ie mapped to '/') as it will not work in this configuration.
 * To enable WebDAV for a context add the following to web.xml:<br/><code>
 * &lt;servlet&gt;<br/>
 *  &lt;servlet-name&gt;webdav&lt;/servlet-name&gt;<br/>
 *  &lt;servlet-class&gt;org.apache.catalina.servlets.WebdavServlet&lt;/servlet-class&gt;<br/>
 *    &lt;init-param&gt;<br/>
 *      &lt;param-name&gt;debug&lt;/param-name&gt;<br/>
 *      &lt;param-value&gt;0&lt;/param-value&gt;<br/>
 *    &lt;/init-param&gt;<br/>
 *    &lt;init-param&gt;<br/>
 *      &lt;param-name&gt;listings&lt;/param-name&gt;<br/>
 *      &lt;param-value&gt;true&lt;/param-value&gt;<br/>
 *    &lt;/init-param&gt;<br/>
 *  &lt;/servlet&gt;<br/>
 *  &lt;servlet-mapping&gt;<br/>
 *    &lt;servlet-name&gt;webdav&lt;/servlet-name&gt;<br/>
 *    &lt;url-pattern&gt;/*&lt;/url-pattern&gt;<br/>
 *  &lt;/servlet-mapping&gt;
 * </code>
 * <p/>
 * This will enable read only access. To enable read-write access add:<br/>
 * <code>
 *    &lt;init-param&gt;<br/>
 *      &lt;param-name&gt;readonly&lt;/param-name&gt;<br/>
 *      &lt;param-value&gt;false&lt;/param-value&gt;<br/>
 *    &lt;/init-param&gt;<br/>
 * </code>
 * <p/>
 * To make the content editable via a different URL, using the following
 * mapping:<br/>
 * <code>
 *  &lt;servlet-mapping&gt;<br/>
 *    &lt;servlet-name&gt;webdav&lt;/servlet-name&gt;<br/>
 *    &lt;url-pattern&gt;/webdavedit/*&lt;/url-pattern&gt;<br/>
 *  &lt;/servlet-mapping&gt;
 * </code>
 * <p/>
 * Don't forget to secure access appropriately to the editing URLs. With this
 * configuration the context will be accessible to normal users as before. Those
 * users with the necessary access will be able to edit content available via
 * http://host:port/context/content using
 * http://host:port/context/webdavedit/content
 *
 * @author Remy Maucherat
 * @version $Revision$ $Date$
 */

public class WebdavServlet



    /**
     * Copy a resource.
     *
     * @param req Servlet request
     * @param resp Servlet response
     * @return boolean true if the copy is successful
     */
    private boolean copyResource(HttpServletRequest req,
                                 HttpServletResponse resp)
        throws ServletException, IOException {

        // Parsing destination header

        String destinationPath = req.getHeader("Destination");

        if (destinationPath == null) {
            resp.sendError(WebdavStatus.SC_BAD_REQUEST);
            return false;
        }

        // Remove url encoding from destination
        destinationPath = RequestUtil.URLDecode(destinationPath, "UTF8");

        int protocolIndex = destinationPath.indexOf("://");
        if (protocolIndex >= 0) {
            // if the Destination URL contains the protocol, we can safely
            // trim everything upto the first "/" character after "://"
            int firstSeparator =
                destinationPath.indexOf("/", protocolIndex + 4);
            if (firstSeparator < 0) {
                destinationPath = "/";
            } else {
                destinationPath = destinationPath.substring(firstSeparator);
            }
        } else {
            String hostName = req.getServerName();
            if ((hostName != null) && (destinationPath.startsWith(hostName))) {
                destinationPath = destinationPath.substring(hostName.length());
            }

            int portIndex = destinationPath.indexOf(":");
            if (portIndex >= 0) {
                destinationPath = destinationPath.substring(portIndex);
            }

            if (destinationPath.startsWith(":")) {
                int firstSeparator = destinationPath.indexOf("/");
                if (firstSeparator < 0) {
                    destinationPath = "/";
                } else {
                    destinationPath =
                        destinationPath.substring(firstSeparator);
                }
            }
        }

        // Normalise destination path (remove '.' and '..')
        destinationPath = normalize(destinationPath);

        String contextPath = req.getContextPath();
        if ((contextPath != null) &&
            (destinationPath.startsWith(contextPath))) {
            destinationPath = destinationPath.substring(contextPath.length());
        }

        String pathInfo = req.getPathInfo();
        if (pathInfo != null) {
            String servletPath = req.getServletPath();
            if ((servletPath != null) &&
                (destinationPath.startsWith(servletPath))) {
                destinationPath = destinationPath
                    .substring(servletPath.length());
            }
        }

        if (debug > 0)
            log("Dest path :" + destinationPath);

        if ((destinationPath.toUpperCase().startsWith("/WEB-INF")) ||
            (destinationPath.toUpperCase().startsWith("/META-INF"))) {
            resp.sendError(WebdavStatus.SC_FORBIDDEN);
            return false;
        }

        String path = getRelativePath(req);

        if ((path.toUpperCase().startsWith("/WEB-INF")) ||
            (path.toUpperCase().startsWith("/META-INF"))) {
            resp.sendError(WebdavStatus.SC_FORBIDDEN);
            return false;
        }

        if (destinationPath.equals(path)) {
            resp.sendError(WebdavStatus.SC_FORBIDDEN);
            return false;
        }

        // Parsing overwrite header

        boolean overwrite = true;
        String overwriteHeader = req.getHeader("Overwrite");

        if (overwriteHeader != null) {
            if (overwriteHeader.equalsIgnoreCase("T")) {
                overwrite = true;
            } else {
                overwrite = false;
            }
        }

        // Overwriting the destination

        boolean exists = true;
        try {
            resources.lookup(destinationPath);
        } catch (NamingException e) {
            exists = false;
        }

        if (overwrite) {

            // Delete destination resource, if it exists
            if (exists) {
                if (!deleteResource(destinationPath, req, resp, true)) {
                    return false;
                }
            } else {
                resp.setStatus(WebdavStatus.SC_CREATED);
            }

        } else {

            // If the destination exists, then it's a conflict
            if (exists) {
                resp.sendError(WebdavStatus.SC_PRECONDITION_FAILED);
                return false;
            }

        }

        // Copying source to destination

        Hashtable<String,Integer> errorList = new Hashtable<String,Integer>();

        boolean result = copyResource(resources, errorList,
                                      path, destinationPath);

        if ((!result) || (!errorList.isEmpty())) {

            sendReport(req, resp, errorList);
            return false;

        }

        // Copy was successful
        resp.setStatus(WebdavStatus.SC_CREATED);

        // Removing any lock-null resource which would be present at
        // the destination path
        lockNullResources.remove(destinationPath);

        return true;

    }





    /**
     * Propfind helper method. Dispays the properties of a lock-null resource.
     *
     * @param resources Resources object associated with this context
     * @param generatedXML XML response to the Propfind request
     * @param path Path of the current resource
     * @param type Propfind type
     * @param propertiesVector If the propfind type is find properties by
     * name, then this Vector contains those properties
     */
    private void parseLockNullProperties(HttpServletRequest req,
                                         XMLWriter generatedXML,
                                         String path, int type,
                                         Vector<String> propertiesVector) {

        // Exclude any resource in the /WEB-INF and /META-INF subdirectories
        // (the "toUpperCase()" avoids problems on Windows systems)
        if (path.toUpperCase().startsWith("/WEB-INF") ||
            path.toUpperCase().startsWith("/META-INF"))
            return;

        // Retrieving the lock associated with the lock-null resource
        LockInfo lock = resourceLocks.get(path);

        if (lock == null)
            return;

        generatedXML.writeElement(null, "response", XMLWriter.OPENING);
        String status = new String("HTTP/1.1 " + WebdavStatus.SC_OK + " "
                                   + WebdavStatus.getStatusText
                                   (WebdavStatus.SC_OK));

        // Generating href element
        generatedXML.writeElement(null, "href", XMLWriter.OPENING);

        String absoluteUri = req.getRequestURI();
        String relativePath = getRelativePath(req);
        String toAppend = path.substring(relativePath.length());
        if (!toAppend.startsWith("/"))
            toAppend = "/" + toAppend;

        generatedXML.writeText(rewriteUrl(normalize(absoluteUri + toAppend)));

        generatedXML.writeElement(null, "href", XMLWriter.CLOSING);

        String resourceName = path;
        int lastSlash = path.lastIndexOf('/');
        if (lastSlash != -1)
            resourceName = resourceName.substring(lastSlash + 1);

        switch (type) {

        case FIND_ALL_PROP :

            generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
            generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

            generatedXML.writeProperty
                (null, "creationdate",
                 getISOCreationDate(lock.creationDate.getTime()));
            generatedXML.writeElement
                (null, "displayname", XMLWriter.OPENING);
            generatedXML.writeData(resourceName);
            generatedXML.writeElement
                (null, "displayname", XMLWriter.CLOSING);
            generatedXML.writeProperty(null, "getlastmodified",
                                       FastHttpDateFormat.formatDate
                                       (lock.creationDate.getTime(), null));
            generatedXML.writeProperty
                (null, "getcontentlength", String.valueOf(0));
            generatedXML.writeProperty(null, "getcontenttype", "");
            generatedXML.writeProperty(null, "getetag", "");
            generatedXML.writeElement(null, "resourcetype",
                                      XMLWriter.OPENING);
            generatedXML.writeElement(null, "lock-null", XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "resourcetype",
                                      XMLWriter.CLOSING);

            generatedXML.writeProperty(null, "source", "");

            String supportedLocks = "<lockentry>"
                + "<lockscope><exclusive/></lockscope>"
                + "<locktype><write/></locktype>"
                + "</lockentry>" + "<lockentry>"
                + "<lockscope><shared/></lockscope>"
                + "<locktype><write/></locktype>"
                + "</lockentry>";
            generatedXML.writeElement(null, "supportedlock",
                                      XMLWriter.OPENING);
            generatedXML.writeText(supportedLocks);
            generatedXML.writeElement(null, "supportedlock",
                                      XMLWriter.CLOSING);

            generateLockDiscovery(path, generatedXML);

            generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "status", XMLWriter.OPENING);
            generatedXML.writeText(status);
            generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

            break;

        case FIND_PROPERTY_NAMES :

            generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
            generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

            generatedXML.writeElement(null, "creationdate",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "displayname",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "getcontentlanguage",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "getcontentlength",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "getcontenttype",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "getetag",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "getlastmodified",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "resourcetype",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "source",
                                      XMLWriter.NO_CONTENT);
            generatedXML.writeElement(null, "lockdiscovery",
                                      XMLWriter.NO_CONTENT);

            generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "status", XMLWriter.OPENING);
            generatedXML.writeText(status);
            generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

            break;

        case FIND_BY_PROPERTY :

            Vector<String> propertiesNotFound = new Vector<String>();

            // Parse the list of properties

            generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
            generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

            Enumeration<String> properties = propertiesVector.elements();

            while (properties.hasMoreElements()) {

                String property = properties.nextElement();

                if (property.equals("creationdate")) {
                    generatedXML.writeProperty
                        (null, "creationdate",
                         getISOCreationDate(lock.creationDate.getTime()));
                } else if (property.equals("displayname")) {
                    generatedXML.writeElement
                        (null, "displayname", XMLWriter.OPENING);
                    generatedXML.writeData(resourceName);
                    generatedXML.writeElement
                        (null, "displayname", XMLWriter.CLOSING);
                } else if (property.equals("getcontentlanguage")) {
                    generatedXML.writeElement(null, "getcontentlanguage",
                                              XMLWriter.NO_CONTENT);
                } else if (property.equals("getcontentlength")) {
                    generatedXML.writeProperty
                        (null, "getcontentlength", (String.valueOf(0)));
                } else if (property.equals("getcontenttype")) {
                    generatedXML.writeProperty
                        (null, "getcontenttype", "");
                } else if (property.equals("getetag")) {
                    generatedXML.writeProperty(null, "getetag", "");
                } else if (property.equals("getlastmodified")) {
                    generatedXML.writeProperty
                        (null, "getlastmodified",
                          FastHttpDateFormat.formatDate
                         (lock.creationDate.getTime(), null));
                } else if (property.equals("resourcetype")) {
                    generatedXML.writeElement(null, "resourcetype",
                                              XMLWriter.OPENING);
                    generatedXML.writeElement(null, "lock-null",
                                              XMLWriter.NO_CONTENT);
                    generatedXML.writeElement(null, "resourcetype",
                                              XMLWriter.CLOSING);
                } else if (property.equals("source")) {
                    generatedXML.writeProperty(null, "source", "");
                } else if (property.equals("supportedlock")) {
                    supportedLocks = "<lockentry>"
                        + "<lockscope><exclusive/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>" + "<lockentry>"
                        + "<lockscope><shared/></lockscope>"
                        + "<locktype><write/></locktype>"
                        + "</lockentry>";
                    generatedXML.writeElement(null, "supportedlock",
                                              XMLWriter.OPENING);
                    generatedXML.writeText(supportedLocks);
                    generatedXML.writeElement(null, "supportedlock",
                                              XMLWriter.CLOSING);
                } else if (property.equals("lockdiscovery")) {
                    if (!generateLockDiscovery(path, generatedXML))
                        propertiesNotFound.addElement(property);
                } else {
                    propertiesNotFound.addElement(property);
                }

            }

            generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "status", XMLWriter.OPENING);
            generatedXML.writeText(status);
            generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
            generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

            Enumeration<String> propertiesNotFoundList = propertiesNotFound.elements();

            if (propertiesNotFoundList.hasMoreElements()) {

                status = new String("HTTP/1.1 " + WebdavStatus.SC_NOT_FOUND
                                    + " " + WebdavStatus.getStatusText
                                    (WebdavStatus.SC_NOT_FOUND));

                generatedXML.writeElement(null, "propstat", XMLWriter.OPENING);
                generatedXML.writeElement(null, "prop", XMLWriter.OPENING);

                while (propertiesNotFoundList.hasMoreElements()) {
                    generatedXML.writeElement
                        (null, propertiesNotFoundList.nextElement(),
                         XMLWriter.NO_CONTENT);
                }

                generatedXML.writeElement(null, "prop", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "status", XMLWriter.OPENING);
                generatedXML.writeText(status);
                generatedXML.writeElement(null, "status", XMLWriter.CLOSING);
                generatedXML.writeElement(null, "propstat", XMLWriter.CLOSING);

            }

            break;

        }

        generatedXML.writeElement(null, "response", XMLWriter.CLOSING);

    }


    
}


