@@ -34,6 +34,7 @@ import org.flywaydb.core.internal.sqlscript.Delimiter;
 import org.flywaydb.core.internal.sqlscript.SqlScript;
 import org.flywaydb.core.internal.sqlscript.SqlScriptFactory;
 import org.flywaydb.core.internal.util.AbbreviationUtils;
+import org.flywaydb.core.internal.util.Pair;
 import org.flywaydb.core.internal.util.StringUtils;
 
 import java.io.Closeable;
@@ -403,12 +404,14 @@ public abstract class Database<C extends Connection> implements Closeable {
                 + " ORDER BY " + quote("installed_rank");
     }
 
-    public String getDeleteStatement(Table table, boolean version) {
-        return "DELETE FROM " + table +
-                " WHERE " + quote("success") + " = " + getBooleanFalse() + " AND " +
-                (version ?
-                        quote("version") + " = ?" :
-                        quote("description") + " = ?");
+    public Pair<String, Object> getDeleteStatement(Table table, boolean version, String filter) {
+        String deleteStatement = "DELETE FROM " + table +
+            " WHERE " + quote("success") + " = " + getBooleanFalse() + " AND " +
+            (version ?
+                quote("version") + " = ?" :
+                quote("description") + " = ?");
+
+        return Pair.of(deleteStatement, filter);
     }
 
     public final String getInstalledBy() {
