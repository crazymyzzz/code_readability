@@ -58,7 +58,7 @@ public class ManagementServiceTest extends PluggableFlowableTestCase {
         assertTrue(assigneeIndex >= 0);
         assertTrue(createTimeIndex >= 0);
 
-        assertOneOf(new String[] { "VARCHAR", "NVARCHAR2", "nvarchar", "NVARCHAR" }, tableMetaData.getColumnTypes().get(assigneeIndex));
+        assertOneOf(new String[] { "VARCHAR", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING" }, tableMetaData.getColumnTypes().get(assigneeIndex));
         assertOneOf(new String[] { "TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME" }, tableMetaData.getColumnTypes().get(createTimeIndex));
     }
 
