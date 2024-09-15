@@ -70,7 +70,7 @@ public class ContentManagementServiceTest extends AbstractFlowableContentTest {
         assertThat(createdIndex).isGreaterThanOrEqualTo(0);
 
         assertThat(tableMetaData.getColumnTypes().get(createdByIndex))
-                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR");
+                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING");
 
         assertThat(tableMetaData.getColumnTypes().get(createdIndex))
                 .isIn("TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME");
