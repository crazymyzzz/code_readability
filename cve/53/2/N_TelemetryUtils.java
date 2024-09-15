

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(Tier.class)
public class TelemetryUtils {

    public static RootTelemetryModel populateRootTelemetry(RootTelemetryModel rootTelemetryModel, Configuration configuration, FlywayPermit flywayPermit) {

        rootTelemetryModel.setApplicationVersion(VersionPrinter.getVersion());

        boolean isRGDomainSet = System.getenv("RGDOMAIN") != null;

        if (flywayPermit != null) {
            rootTelemetryModel.setRedgateEmployee(flywayPermit.isRedgateEmployee() || isRGDomainSet);
            rootTelemetryModel.setApplicationEdition(flywayPermit.getTier().asString());
            rootTelemetryModel.setTrial(flywayPermit.isTrial());
            rootTelemetryModel.setSignedIn(flywayPermit.isFromAuth());
        } else {
            rootTelemetryModel.setRedgateEmployee(isRGDomainSet);
        }

        if (configuration != null) {
            ConfigurationModel modernConfig = configuration.getModernConfig();
            if (modernConfig != null && StringUtils.hasText(modernConfig.getId())) {
                rootTelemetryModel.setProjectId(EncryptionUtils.hashString(modernConfig.getId(), "fur"));
            }
        }

        return rootTelemetryModel;
    }

    
}