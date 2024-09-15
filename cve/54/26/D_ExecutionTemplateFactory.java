@@ -23,15 +23,6 @@ import org.flywaydb.core.internal.database.base.Table;
 import java.sql.Connection;
 
 public class ExecutionTemplateFactory {
-    /**
-     * Creates a new execution template for this connection.
-     * If possible, will attempt to roll back when an exception is thrown.
-     *
-     * @param connection The connection for execution.
-     */
-    public static ExecutionTemplate createExecutionTemplate(Connection connection) {
-        return createTransactionalExecutionTemplate(connection, true, DatabaseTypeRegister.getDatabaseTypeForConnection(connection));
-    }
 
     /**
      * Creates a new execution template for this connection.
