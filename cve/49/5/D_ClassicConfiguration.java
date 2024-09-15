@@ -136,7 +136,9 @@ public class ClassicConfiguration implements Configuration {
     }
 
     @Override
-    public String getReportFilename() { return getModernFlyway().getReportFilename(); }
+    public String getReportFilename() {
+        return getModernFlyway().getReportFilename();
+    }
 
     @Getter
     @Setter
