@@ -75,7 +75,7 @@ public class DmnManagementServiceTest extends AbstractFlowableDmnTest {
         assertThat(startTimeIndex).isGreaterThanOrEqualTo(0);
 
         assertThat(tableMetaData.getColumnTypes().get(instanceIdIndex))
-                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR");
+                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING");
 
         assertThat(tableMetaData.getColumnTypes().get(startTimeIndex))
                 .isIn("TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME");
