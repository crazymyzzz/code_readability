@@ -820,20 +820,6 @@ public class DefaultServlet extends HttpServlet {
             return;
         }
 
-        // If the resource is not a collection, and the resource path
-        // ends with "/" or "\", return NOT FOUND
-        if (resource.isFile() && (path.endsWith("/") || path.endsWith("\\"))) {
-            // Check if we're included so we can return the appropriate
-            // missing resource name in the error
-            String requestUri = (String) request.getAttribute(
-                    RequestDispatcher.INCLUDE_REQUEST_URI);
-            if (requestUri == null) {
-                requestUri = request.getRequestURI();
-            }
-            response.sendError(HttpServletResponse.SC_NOT_FOUND, requestUri);
-            return;
-        }
-
         boolean included = false;
         // Check if the conditions specified in the optional If headers are
         // satisfied.
