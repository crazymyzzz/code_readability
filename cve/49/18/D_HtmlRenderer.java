@@ -17,28 +17,13 @@ package org.flywaydb.core.extensibility;
 
 import org.flywaydb.core.api.configuration.Configuration;
 import org.flywaydb.core.api.output.HtmlResult;
-
-import java.util.Collections;
-
-import static org.flywaydb.core.internal.reports.html.HtmlReportGenerator.getInformationalText;
-import static org.flywaydb.core.internal.reports.html.HtmlReportGenerator.getSummaryStyle;
+import java.util.List;
 
 public interface HtmlRenderer<T extends HtmlResult> extends Plugin {
     String render(T result, Configuration config);
-
-    String tabTitle(T result);
-
+    String tabTitle(T result, Configuration config);
     Class<T> getType();
-
-    default String getSummary(T result) {
-        String separator = String.join("", Collections.nCopies(8, "&nbsp;"));
-        return "<div " + getSummaryStyle() + ">\n" +
-                "<div>" + getSummaryText(result, separator, true) + "</div>" +
-                "<div>" + getInformationalText() + "</div>\n" +
-                "</div>\n";
-    }
-
-   default String getSummaryText(T result, String separator, boolean addStyles) {
+    default List<HtmlReportSummary> getHtmlSummary(T result) {
         return null;
     }
 }
\ No newline at end of file
