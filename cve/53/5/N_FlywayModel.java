

@Getter
@Setter
@NoArgsConstructor
@ExtensionMethod(MergeUtils.class)
public class FlywayModel {


    public FlywayModel merge(FlywayModel otherPojo) {
        FlywayModel result = new FlywayModel();
        result.outputProgress = outputProgress.merge(otherPojo.outputProgress);
        result.outputType = outputType.merge(otherPojo.outputType);
        result.reportFilename = reportFilename.merge(otherPojo.reportFilename);
        result.encoding = encoding.merge(otherPojo.encoding);
        result.environment = environment.merge(otherPojo.environment);
        result.environmentProvisionMode = environmentProvisionMode.merge(otherPojo.environmentProvisionMode);
        result.detectEncoding = detectEncoding.merge(otherPojo.detectEncoding);
        result.placeholderPrefix = placeholderPrefix.merge(otherPojo.placeholderPrefix);
        result.placeholderSuffix = placeholderSuffix.merge(otherPojo.placeholderSuffix);
        result.placeholderSeparator = placeholderSeparator.merge(otherPojo.placeholderSeparator);
        result.scriptPlaceholderPrefix = scriptPlaceholderPrefix.merge(otherPojo.scriptPlaceholderPrefix);
        result.scriptPlaceholderSuffix = scriptPlaceholderSuffix.merge(otherPojo.scriptPlaceholderSuffix);
        result.sqlMigrationPrefix = sqlMigrationPrefix.merge(otherPojo.sqlMigrationPrefix);
        result.executeInTransaction = executeInTransaction.merge(otherPojo.executeInTransaction);
        result.repeatableSqlMigrationPrefix = repeatableSqlMigrationPrefix.merge(otherPojo.repeatableSqlMigrationPrefix);
        result.sqlMigrationSeparator = sqlMigrationSeparator.merge(otherPojo.sqlMigrationSeparator);
        result.sqlMigrationSuffixes = sqlMigrationSuffixes.merge(otherPojo.sqlMigrationSuffixes);
        result.cleanDisabled = cleanDisabled.merge(otherPojo.cleanDisabled);
        result.cleanOnValidationError = cleanOnValidationError.merge(otherPojo.cleanOnValidationError);
        result.locations = locations.merge(otherPojo.locations);
        result.table = table.merge(otherPojo.table);
        result.tablespace = tablespace.merge(otherPojo.tablespace);
        result.target = target.merge(otherPojo.target);
        result.failOnMissingTarget = failOnMissingTarget.merge(otherPojo.failOnMissingTarget);
        result.placeholderReplacement = placeholderReplacement.merge(otherPojo.placeholderReplacement);
        result.ignoreMigrationPatterns = ignoreMigrationPatterns.merge(otherPojo.ignoreMigrationPatterns);
        result.validateMigrationNaming = validateMigrationNaming.merge(otherPojo.validateMigrationNaming);
        result.validateOnMigrate = validateOnMigrate.merge(otherPojo.validateOnMigrate);
        result.baselineVersion = baselineVersion.merge(otherPojo.baselineVersion);
        result.baselineDescription = baselineDescription.merge(otherPojo.baselineDescription);
        result.baselineOnMigrate = baselineOnMigrate.merge(otherPojo.baselineOnMigrate);
        result.outOfOrder = outOfOrder.merge(otherPojo.outOfOrder);
        result.skipExecutingMigrations = skipExecutingMigrations.merge(otherPojo.skipExecutingMigrations);
        result.callbacks = callbacks.merge(otherPojo.callbacks);
        result.skipDefaultCallbacks = skipDefaultCallbacks.merge(otherPojo.skipDefaultCallbacks);
        result.migrationResolvers = migrationResolvers.merge(otherPojo.migrationResolvers);
        result.skipDefaultResolvers = skipDefaultResolvers.merge(otherPojo.skipDefaultResolvers);
        result.mixed = mixed.merge(otherPojo.mixed);
        result.group = group.merge(otherPojo.group);
        result.installedBy = installedBy.merge(otherPojo.installedBy);
        result.createSchemas = createSchemas.merge(otherPojo.createSchemas);
        result.errorOverrides = errorOverrides.merge(otherPojo.errorOverrides);
        result.dryRunOutput = dryRunOutput.merge(otherPojo.dryRunOutput);
        result.stream = stream.merge(otherPojo.stream);
        result.batch = batch.merge(otherPojo.batch);
        result.outputQueryResults = outputQueryResults.merge(otherPojo.outputQueryResults);
        result.lockRetryCount = lockRetryCount.merge(otherPojo.lockRetryCount);
        result.kerberosConfigFile = kerberosConfigFile.merge(otherPojo.kerberosConfigFile);
        result.failOnMissingLocations = failOnMissingLocations.merge(otherPojo.failOnMissingLocations);
        result.loggers = loggers.merge(otherPojo.loggers);
        result.defaultSchema = defaultSchema.merge(otherPojo.defaultSchema);
        result.placeholders = MergeUtils.merge(placeholders, otherPojo.placeholders, (a,b) -> b != null ? b : a);
        result.reportEnabled = reportEnabled.merge(otherPojo.reportEnabled);
        result.propertyResolvers = MergeUtils.merge(propertyResolvers, otherPojo.propertyResolvers, (a,b) -> b != null ? b : a); // TODO: more granular merge
        result.pluginConfigurations = MergeUtils.merge(pluginConfigurations, otherPojo.pluginConfigurations, (a,b) -> b != null ? b : a);
        return result;
    }


}