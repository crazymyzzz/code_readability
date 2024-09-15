

/**
 * Supports reading and writing to the schema history table.
 */
@CustomLog
class JdbcTableSchemaHistory extends SchemaHistory {


    @Override
    public boolean removeFailedMigrations(RepairResult repairResult, MigrationPattern[] migrationPatternFilter) {
        if (!exists()) {
            LOG.info("Repair of failed migration in Schema History table " + table + " not necessary as table doesn't exist.");
            return false;
        }

        List<AppliedMigration> appliedMigrations = filterMigrations(allAppliedMigrations(), migrationPatternFilter);

        boolean failed = appliedMigrations.stream().anyMatch(am -> !am.isSuccess());
        if (!failed) {
            LOG.info("Repair of failed migration in Schema History table " + table + " not necessary. No failed migration detected.");
            return false;
        }

        try {
            appliedMigrations.stream()
                             .filter(am -> !am.isSuccess())
                             .forEach(am -> repairResult.migrationsRemoved.add(CommandResultFactory.createRepairOutput(am)));

            for (AppliedMigration appliedMigration : appliedMigrations) {
                if (appliedMigration.getVersion() != null) {
                    jdbcTemplate.execute(database.getDeleteStatement(table, true ), appliedMigration.getVersion().getVersion());
                } else {
                    jdbcTemplate.execute(database.getDeleteStatement(table, false ), appliedMigration.getDescription());
                }

            }

            clearCache();
        } catch (SQLException e) {
            throw new FlywaySqlException("Unable to repair Schema History table " + table, e);
        }

        return true;
    }


}