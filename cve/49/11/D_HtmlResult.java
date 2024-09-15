@@ -15,24 +15,44 @@
  */
 package org.flywaydb.core.api.output;
 
+import lombok.Getter;
 import lombok.NoArgsConstructor;
+import lombok.Setter;
 
 import java.time.LocalDateTime;
 
 @NoArgsConstructor
 public class HtmlResult implements OperationResult {
-    public LocalDateTime timestamp;
-    public String operation;
-    public String exception;
+    @Getter
+    @Setter
+    private String timestamp;
+    @Getter
+    @Setter
+    private String operation;
+    @Getter
+    private String exception;
     public transient Exception exceptionObject;
+    @Getter
+    @Setter
+    private boolean licenseFailed;
 
     protected HtmlResult(LocalDateTime timestamp, String operation) {
-        this.timestamp = timestamp;
+        setTimestamp(timestamp);
         this.operation = operation;
     }
 
-    public void addException(Exception exception) {
-        this.exceptionObject = exception;
-        this.exception = exception.getMessage();
+    public void setTimestamp(LocalDateTime timestamp) {
+        this.timestamp = timestamp.toString();
+    }
+
+    public LocalDateTime getTimestamp() {
+        return LocalDateTime.parse(timestamp);
+    }
+
+    public void setException(Exception exception) {
+        if (exception != null) {
+            this.exceptionObject = exception;
+            this.exception = exception.getMessage();
+        }
     }
 }
\ No newline at end of file
