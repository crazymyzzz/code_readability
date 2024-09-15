
@@ -106,11 +107,7 @@ public final class Grok {
         this.namedCaptures = namedCaptures;
         this.matcherWatchdog = matcherWatchdog;
 
-        for (Map.Entry<String, String> entry : patternBank.entrySet()) {
-            String name = entry.getKey();
-            String pattern = entry.getValue();
-            forbidCircularReferences(name, new ArrayList<>(), pattern);
-        }
+        validatePatternBank();
 
         String expression = toRegex(grokPattern);
         byte[] expressionBytes = expression.getBytes(StandardCharsets.UTF_8);
@@ -125,46 +122,68 @@ public final class Grok {
     }
 
     /**
-     * Checks whether patterns reference each other in a circular manner and if so fail with an exception
+     * Entry point to recursively validate the pattern bank for circular dependencies and malformed URLs
+     * via depth-first traversal. This implementation does not include memoization.
+     */
+    private void validatePatternBank() {
+        for (String patternName : patternBank.keySet()) {
+            validatePatternBank(patternName, new Stack<>());
+        }
+    }
+
+    /**
+     * Checks whether patterns reference each other in a circular manner and, if so, fail with an exception.
+     * Also checks for malformed pattern definitions and fails with an exception.
      *
      * In a pattern, anything between <code>%{</code> and <code>}</code> or <code>:</code> is considered
      * a reference to another named pattern. This method will navigate to all these named patterns and
      * check for a circular reference.
      */
-    private void forbidCircularReferences(String patternName, List<String> path, String pattern) {
-        if (pattern.contains("%{" + patternName + "}") || pattern.contains("%{" + patternName + ":")) {
-            String message;
-            if (path.isEmpty()) {
-                message = "circular reference in pattern [" + patternName + "][" + pattern + "]";
-            } else {
-                message = "circular reference in pattern [" + path.remove(path.size() - 1) + "][" + pattern +
-                    "] back to pattern [" + patternName + "]";
-                // add rest of the path:
-                if (path.isEmpty() == false) {
-                    message += " via patterns [" + String.join("=>", path) + "]";
-                }
-            }
-            throw new IllegalArgumentException(message);
+    private void validatePatternBank(String patternName, Stack<String> path) {
+        String pattern = patternBank.get(patternName);
+        boolean isSelfReference = pattern.contains("%{" + patternName + "}") ||
+                                    pattern.contains("%{" + patternName + ":");
+        if (isSelfReference) {
+            throwExceptionForCircularReference(patternName, pattern);
+        } else if (path.contains(patternName)) {
+            // current pattern name is already in the path, fetch its predecessor
+            String prevPatternName = path.pop();
+            String prevPattern = patternBank.get(prevPatternName);
+            throwExceptionForCircularReference(prevPatternName, prevPattern, patternName, path);
         }
-
+        path.push(patternName);
         for (int i = pattern.indexOf("%{"); i != -1; i = pattern.indexOf("%{", i + 1)) {
             int begin = i + 2;
-            int brackedIndex = pattern.indexOf('}', begin);
-            int columnIndex = pattern.indexOf(':', begin);
-            int end;
-            if (brackedIndex != -1 && columnIndex == -1) {
-                end = brackedIndex;
-            } else if (columnIndex != -1 && brackedIndex == -1) {
-                end = columnIndex;
-            } else if (brackedIndex != -1 && columnIndex != -1) {
-                end = Math.min(brackedIndex, columnIndex);
-            } else {
-                throw new IllegalArgumentException("pattern [" + pattern + "] has circular references to other pattern definitions");
+            int syntaxEndIndex = pattern.indexOf('}', begin);
+            if (syntaxEndIndex == -1) {
+                throw new IllegalArgumentException("Malformed pattern [" + patternName + "][" + pattern +"]");
+            }
+            int semanticNameIndex = pattern.indexOf(':', begin);
+            int end = syntaxEndIndex;
+            if (semanticNameIndex != -1) {
+                end = Math.min(syntaxEndIndex, semanticNameIndex);
             }
-            String otherPatternName = pattern.substring(begin, end);
-            path.add(otherPatternName);
-            forbidCircularReferences(patternName, path, patternBank.get(otherPatternName));
+            String dependsOnPattern = pattern.substring(begin, end);
+            validatePatternBank(dependsOnPattern, path);
+        }
+        path.pop();
+    }
+
+    private static void throwExceptionForCircularReference(String patternName, String pattern) {
+        throwExceptionForCircularReference(patternName, pattern, null, null);
+    }
+
+    private static void throwExceptionForCircularReference(String patternName, String pattern, String originPatterName,
+                                                           Stack<String> path) {
+        StringBuilder message = new StringBuilder("circular reference in pattern [");
+        message.append(patternName).append("][").append(pattern).append("]");
+        if (originPatterName != null) {
+            message.append(" back to pattern [").append(originPatterName).append("]");
+        }
+        if (path != null && path.size() > 1) {
+            message.append(" via patterns [").append(String.join("=>", path)).append("]");
         }
+        throw new IllegalArgumentException(message.toString());
     }

