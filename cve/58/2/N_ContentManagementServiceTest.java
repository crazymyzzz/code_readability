

public class ContentManagementServiceTest extends AbstractFlowableContentTest {



    @Test
    public void testGetTableMetaData() {

        String tablePrefix = contentEngineConfiguration.getDatabaseTablePrefix();

        TableMetaData tableMetaData = contentManagementService.getTableMetaData(tablePrefix + "ACT_CO_CONTENT_ITEM");
        assertThat(tableMetaData.getColumnTypes()).hasSameSizeAs(tableMetaData.getColumnNames());
        assertThat(tableMetaData.getColumnNames()).hasSize(17);

        int createdByIndex = tableMetaData.getColumnNames().indexOf("CREATED_BY_");
        int createdIndex = tableMetaData.getColumnNames().indexOf("CREATED_");

        assertThat(createdByIndex).isGreaterThanOrEqualTo(0);
        assertThat(createdIndex).isGreaterThanOrEqualTo(0);

        assertThat(tableMetaData.getColumnTypes().get(createdByIndex))
                .isIn("VARCHAR", "VARCHAR2", "NVARCHAR2", "nvarchar", "NVARCHAR", "CHARACTER VARYING");

        assertThat(tableMetaData.getColumnTypes().get(createdIndex))
                .isIn("TIMESTAMP", "TIMESTAMP(6)", "datetime", "DATETIME");
    }
}
