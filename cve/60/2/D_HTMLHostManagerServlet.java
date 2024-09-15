
@@ -195,7 +196,11 @@ public final class HTMLHostManagerServlet extends HostManagerServlet {
         // Message Section
         args = new Object[3];
         args[0] = sm.getString("htmlHostManagerServlet.messageLabel");
-        args[1] = (message == null || message.length() == 0) ? "OK" : message;
+        if (message == null || message.length() == 0) {
+            args[1] = "OK";
+        } else {
+            args[1] = RequestUtil.filter(message);
+        }
         writer.print(MessageFormat.format(Constants.MESSAGE_SECTION, args));
 
         // Manager Section
