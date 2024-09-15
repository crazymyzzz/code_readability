
@@ -344,7 +344,17 @@ public class GrokTests extends OpenSearchTestCase {
             String pattern = "%{NAME1}";
             new Grok(bank, pattern, false, logger::warn);
         });
-        assertEquals("circular reference in pattern [NAME3][!!!%{NAME1}!!!] back to pattern [NAME1] via patterns [NAME2]",
+        assertEquals("circular reference in pattern [NAME3][!!!%{NAME1}!!!] back to pattern [NAME1] via patterns [NAME1=>NAME2]",
+            e.getMessage());
+
+        e = expectThrows(IllegalArgumentException.class, () -> {
+            Map<String, String> bank = new TreeMap<>();
+            bank.put("NAME1", "!!!%{NAME2}!!!");
+            bank.put("NAME2", "!!!%{NAME2}!!!");
+            String pattern = "%{NAME1}";
+            new Grok(bank, pattern, false, logger::warn);
+        });
+        assertEquals("circular reference in pattern [NAME2][!!!%{NAME2}!!!]",
             e.getMessage());
 
         e = expectThrows(IllegalArgumentException.class, () -> {
@@ -358,7 +368,25 @@ public class GrokTests extends OpenSearchTestCase {
             new Grok(bank, pattern, false, logger::warn );
         });
         assertEquals("circular reference in pattern [NAME5][!!!%{NAME1}!!!] back to pattern [NAME1] " +
-            "via patterns [NAME2=>NAME3=>NAME4]", e.getMessage());
+            "via patterns [NAME1=>NAME2=>NAME3=>NAME4]", e.getMessage());
+    }
