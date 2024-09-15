

public class DmnManagementServiceTest extends AbstractFlowableDmnTest {



    @Test
    public void testGetTableMetaData() {

        String tablePrefix = dmnEngineConfiguration.getDatabaseTablePrefix();

        TableMetaData tableMetaData = managementService.getTableMetaData(tablePrefix + "ACT_DMN_HI_DECISION_EXECUTION");
        assertThat(tableMetaData.getColumnTypes()).hasSameSizeAs(tableMetaData.getColumnNames());
        assertThat(tableMetaData.getColumnNames()).hasSize(12);

        int instanceIdIndex = tableMetaData.getColumnNames().indexOf("INSTANCE_ID_");
        int startTimeIndex = tableMetaData.getColumnNames().indexOf("START_TIME_");

        assertThat(instanceIdIndex).isGreaterThanOrEqualTo(0);
        assertThat(startTimeIndex).isGreaterThanOrEqualTo(0);

        assertThat(tableMetaData.getColumnTypes().get(instanceIdIndex))
                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING");

        assertThat(tableMetaData.getColumnTypes().get(startTimeIndex))
                .isIn("TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME");
    }
}
