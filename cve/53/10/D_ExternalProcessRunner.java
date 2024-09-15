
@@ -26,10 +27,21 @@ import java.util.concurrent.Future;
 import java.util.function.Consumer;
 
 public class ExternalProcessRunner {
+
     @SneakyThrows
     public int run(final String[] command, final Consumer<? super String> onStdOut,
         final Consumer<? super String> onStdErr) {
-        final Process process = new ProcessBuilder(command).start();
+        return run(command, null, onStdOut, onStdErr);
+    }
+
+    @SneakyThrows
+    public int run(final String[] command, final Path workingDirectory,
+        final Consumer<? super String> onStdOut, final Consumer<? super String> onStdErr) {
+        final ProcessBuilder processBuilder = new ProcessBuilder(command);
+        if (workingDirectory != null) {
+            processBuilder.directory(workingDirectory.toFile());
+        }
+        final Process process = processBuilder.start();
 
         final ExecutorService executorService = Executors.newFixedThreadPool(2);
         try (final InputStreamReader outStream = new InputStreamReader(process.getInputStream(),
