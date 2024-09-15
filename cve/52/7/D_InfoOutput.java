@@ -20,10 +20,10 @@ import lombok.Getter;
 import lombok.NoArgsConstructor;
 import lombok.Setter;
 
-@AllArgsConstructor
-@NoArgsConstructor
 @Getter
 @Setter
+@NoArgsConstructor
+@AllArgsConstructor
 public class InfoOutput {
     public String category;
     public String version;
@@ -36,5 +36,24 @@ public class InfoOutput {
     public String filepath;
     public String undoFilepath;
     public String installedBy;
+    public String shouldExecuteExpression;
     public int executionTime;
+
+    public InfoOutput(String category, String version, String rawVersion, String description, String type,
+        String installedOnUTC, String state, String undoable, String filepath, String undoFilepath, String installedBy,
+        int executionTime) {
+        this.category = category;
+        this.version = version;
+        this.rawVersion = rawVersion;
+        this.description = description;
+        this.type = type;
+        this.installedOnUTC = installedOnUTC;
+        this.state = state;
+        this.undoable = undoable;
+        this.filepath = filepath;
+        this.undoFilepath = undoFilepath;
+        this.installedBy = installedBy;
+        this.executionTime = executionTime;
+        this.shouldExecuteExpression = null;
+    }
 }
\ No newline at end of file
