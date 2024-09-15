

@CustomLog
public class SnowflakeDatabase extends Database<SnowflakeConnection> {



    @Override
    public void ensureSupported() {
        ensureDatabaseIsRecentEnough("3.0");

        ensureDatabaseNotOlderThanOtherwiseRecommendUpgradeToFlywayEdition("3", org.flywaydb.core.internal.license.Edition.ENTERPRISE);

        recommendFlywayUpgradeIfNecessaryForMajorVersion("7.33");
    }

   
}