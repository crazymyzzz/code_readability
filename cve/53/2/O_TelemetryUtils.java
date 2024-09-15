

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(Tier.class)
public class TelemetryUtils {

    public static RootTelemetryModel populateRootTelemetry(RootTelemetryModel rootTelemetryModel, Configuration configuration, boolean isRedgateEmployee) {
        rootTelemetryModel.setRedgateEmployee(isRedgateEmployee);

        if (configuration != null) {
            String currentTier = LicenseGuard.getTierAsString(configuration);
            rootTelemetryModel.setApplicationEdition(currentTier);
            rootTelemetryModel.setApplicationVersion(VersionPrinter.getVersion());
            rootTelemetryModel.setTrial(LicenseGuard.getPermit(configuration).isTrial());
            ConfigurationModel modernConfig = configuration.getModernConfig();
            if (modernConfig != null && StringUtils.hasText(modernConfig.getId())) {
                rootTelemetryModel.setProjectId(EncryptionUtils.hashString(modernConfig.getId(), "fur"));
            }
        }

        return rootTelemetryModel;
    }

    
}