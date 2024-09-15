
@@ -281,7 +292,8 @@ public static void outputLog(Writer writer) {
                         TimeData td = times.get(i);
                         if (td != null) {
                             writer.write(i + " " + td.getMessage() + ": " +
-                                         td.getTime() + "\n");
+                                         (td.getTime() - baseTime) + "\n");
+
                         }
                     }
                 }
