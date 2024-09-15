@@ -54,7 +54,7 @@ public class JspHelper {
     }
     private static String localeToString(Locale locale) {
         if (locale != null) {
-            return locale.toString();//locale.getDisplayName();
+            return escapeXml(locale.toString());//locale.getDisplayName();
         } else {
             return "";
         }
