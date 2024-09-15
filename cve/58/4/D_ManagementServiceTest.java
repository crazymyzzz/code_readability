@@ -88,7 +88,9 @@ public class ManagementServiceTest extends PluggableFlowableTestCase {
         assertThat(assigneeIndex).isGreaterThanOrEqualTo(0);
         assertThat(createTimeIndex).isGreaterThanOrEqualTo(0);
 
-        assertOneOf(new String[] { "VARCHAR", "NVARCHAR2", "nvarchar", "NVARCHAR" }, tableMetaData.getColumnTypes().get(assigneeIndex));
+        List<String> test = tableMetaData.getColumnTypes();
+
+        assertOneOf(new String[] { "VARCHAR", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING" }, tableMetaData.getColumnTypes().get(assigneeIndex));
         assertOneOf(new String[] { "TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME" }, tableMetaData.getColumnTypes().get(createTimeIndex));
     }
 
