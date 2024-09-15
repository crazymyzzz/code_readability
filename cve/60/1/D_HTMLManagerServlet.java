@@ -130,8 +130,7 @@ public final class HTMLManagerServlet extends ManagerServlet {
             message = stop(path);
         } else {
             message =
-                sm.getString("managerServlet.unknownCommand",
-                             RequestUtil.filter(command));
+                sm.getString("managerServlet.unknownCommand", command);
         }
 
         list(request, response, message);
@@ -305,7 +304,11 @@ public final class HTMLManagerServlet extends ManagerServlet {
         // Message Section
         args = new Object[3];
         args[0] = sm.getString("htmlManagerServlet.messageLabel");
-        args[1] = (message == null || message.length() == 0) ? "OK" : message;
+        if (message == null || message.length() == 0) {
+            args[1] = "OK";
+        } else {
+            args[1] = RequestUtil.filter(message);
+        }
         writer.print(MessageFormat.format(Constants.MESSAGE_SECTION, args));
 
         // Manager Section
