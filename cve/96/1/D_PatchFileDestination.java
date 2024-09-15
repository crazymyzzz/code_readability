
@@ -60,12 +59,7 @@ public final class PatchFileDestination implements FileDestination {
     if (!oldSource.equals(newSource)) {
       List<String> originalLines = LINE_SPLITTER.splitToList(oldSource);
 
-      Patch<String> diff = null;
-      try {
-        diff = DiffUtils.diff(originalLines, LINE_SPLITTER.splitToList(newSource));
-      } catch (DiffException e) {
-        throw new AssertionError("DiffUtils.diff should not fail", e);
-      }
+      Patch<String> diff = DiffUtils.diff(originalLines, LINE_SPLITTER.splitToList(newSource));
       String relativePath = baseDir.relativize(sourceFilePath).toString();
       List<String> unifiedDiff =
           UnifiedDiffUtils.generateUnifiedDiff(relativePath, relativePath, originalLines, diff, 2);
