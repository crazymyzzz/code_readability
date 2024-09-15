@@ -20,6 +20,7 @@ import lombok.NoArgsConstructor;
 import lombok.Setter;
 import lombok.experimental.ExtensionMethod;
 import org.flywaydb.core.internal.util.MergeUtils;
+import org.flywaydb.core.internal.util.StringUtils;
 
 import java.util.ArrayList;
 import java.util.Arrays;
@@ -33,6 +34,7 @@ import java.util.Map;
 @NoArgsConstructor
 @ExtensionMethod(MergeUtils.class)
 public class FlywayModel {
+    @Setter(lombok.AccessLevel.NONE)
     private String reportFilename;
     private String environment;
     private Boolean detectEncoding;
@@ -93,7 +95,7 @@ public class FlywayModel {
 
     public static FlywayModel defaults(){
          FlywayModel model = new FlywayModel();
-         model.reportFilename = "report.html";
+         model.reportFilename = "report";
          model.detectEncoding = false;
          model.encoding = "UTF-8";
          model.executeInTransaction = true;
@@ -142,6 +144,7 @@ public class FlywayModel {
          model.failOnMissingLocations = false;
          model.loggers = Arrays.asList("auto");
          model.placeholders = new HashMap<>();
+         model.environment = "default";
          return model;
     }
 
@@ -206,4 +209,10 @@ public class FlywayModel {
         result.propertyResolvers = MergeUtils.merge(propertyResolvers, otherPojo.propertyResolvers, (a,b) -> b != null ? b : a); // TODO: more granular merge
         return result;
     }
+
+    public void setReportFilename(String reportFilename) {
+        if (StringUtils.hasText(reportFilename)) {
+            this.reportFilename = reportFilename;
+        }
+    }
 }
\ No newline at end of file
