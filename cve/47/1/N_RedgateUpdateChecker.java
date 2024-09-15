
@CustomLog
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(StringUtils.class)
public class RedgateUpdateChecker {


    public static boolean isEnabled() {
        return !Boolean.parseBoolean(System.getenv("REDGATE_DISABLE_TELEMETRY")) && usageChecker("flyway-cfu", VersionPrinter.getVersion());
    }

    
}