
+@AllArgsConstructor
 public class VersionResult implements OperationResult {
     public String version;
     public String command;
-
-    public VersionResult(String version, String command) {
-        this.version = version;
-        this.command = command;
-    }
+    public Edition edition;
 }
\ No newline at end of file
