@@ -52,6 +52,7 @@ import javax.servlet.http.HttpSession;
 import org.apache.catalina.util.IOTools;
 import org.apache.juli.logging.Log;
 import org.apache.juli.logging.LogFactory;
+import org.apache.tomcat.util.compat.JrePlatform;
 import org.apache.tomcat.util.res.StringManager;
 
 
@@ -245,10 +246,21 @@ public final class CGIServlet extends HttpServlet {
     private static final long serialVersionUID = 1L;
 
     private static final Set<String> DEFAULT_SUPER_METHODS = new HashSet<>();
+    private static final Pattern DEFAULT_CMD_LINE_ARGUMENTS_DECODED_PATTERN;
+    private static final String ALLOW_ANY_PATTERN = ".*";
+
     static {
         DEFAULT_SUPER_METHODS.add("HEAD");
         DEFAULT_SUPER_METHODS.add("OPTIONS");
         DEFAULT_SUPER_METHODS.add("TRACE");
+
+        if (JrePlatform.IS_WINDOWS) {
+            DEFAULT_CMD_LINE_ARGUMENTS_DECODED_PATTERN = Pattern.compile("[a-zA-Z0-9\\Q-_.\\/:\\E]+");
+        } else {
+            // No restrictions
+            DEFAULT_CMD_LINE_ARGUMENTS_DECODED_PATTERN = null;
+        }
+
     }
 
 
@@ -314,6 +326,14 @@ public final class CGIServlet extends HttpServlet {
     private Pattern cmdLineArgumentsEncodedPattern =
             Pattern.compile("[a-zA-Z0-9\\Q%;/?:@&,$-_.!~*'()\\E]+");
 
+    /**
+     * Limits the decoded form of individual command line arguments. Default
+     * varies by platform.
+     */
+    private Pattern cmdLineArgumentsDecodedPattern = DEFAULT_CMD_LINE_ARGUMENTS_DECODED_PATTERN;
+
+
+
     /**
      * Sets instance variables.
      * <P>
@@ -411,6 +431,14 @@ public final class CGIServlet extends HttpServlet {
             cmdLineArgumentsEncodedPattern =
                     Pattern.compile(getServletConfig().getInitParameter("cmdLineArgumentsEncoded"));
         }
+
+        String value = getServletConfig().getInitParameter("cmdLineArgumentsDecoded");
+        if (ALLOW_ANY_PATTERN.equals(value)) {
+            // Optimisation for case where anything is allowed
+            cmdLineArgumentsDecodedPattern = null;
+        } else if (value != null) {
+            cmdLineArgumentsDecodedPattern = Pattern.compile(value);
+        }
     }
 
 
@@ -792,7 +820,17 @@ public final class CGIServlet extends HttpServlet {
                             }
                             return false;
                         }
+
                         String decodedArgument = URLDecoder.decode(encodedArgument, parameterEncoding);
+                        if (cmdLineArgumentsDecodedPattern != null &&
+                                !cmdLineArgumentsDecodedPattern.matcher(decodedArgument).matches()) {
+                            if (log.isDebugEnabled()) {
+                                log.debug(sm.getString("cgiServlet.invalidArgumentDecoded",
+                                        decodedArgument, cmdLineArgumentsDecodedPattern.toString()));
+                            }
+                            return false;
+                        }
+
                         cmdLineParameters.add(decodedArgument);
                     }
                 }
@@ -1101,7 +1139,6 @@ public final class CGIServlet extends HttpServlet {
             this.env = envp;
 
             return true;
-
         }
 
         /**
