@@ -22,6 +22,7 @@ import org.flywaydb.core.api.FlywayException;
 import org.flywaydb.core.api.output.CompositeResult;
 
 import java.io.FileWriter;
+import java.time.format.DateTimeFormatter;
 
 import static org.flywaydb.core.internal.reports.html.HtmlReportGenerator.generateHtml;
 
@@ -37,6 +38,13 @@ public class HtmlUtils {
         return filename;
     }
 
+    public static String getFormattedTimestamp(HtmlResult result) {
+        if (result == null || result.getTimestamp() == null) {
+            return "--";
+        }
+        return result.getTimestamp().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
+    }
+
     public static String htmlEncode(String input) {
         return StringEscapeUtils.escapeHtml4(input);
     }
