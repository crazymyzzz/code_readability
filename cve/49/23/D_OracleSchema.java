@@ -815,7 +815,9 @@ public class OracleSchema extends Schema<OracleDatabase, OracleTable> {
 
 
                                     "UNION SELECT '" + CREDENTIAL.getName() + "' FROM DUAL WHERE EXISTS(" +
-                                            "SELECT * FROM ALL_SCHEDULER_CREDENTIALS WHERE OWNER = ?) "
+                                            "SELECT * FROM "+
+                                            (database.getVersion().isAtLeast("12.1")?"ALL_CREDENTIALS":"ALL_SCHEDULER_CREDENTIALS")
+                                            +" WHERE OWNER = ?) "
 
 
 
