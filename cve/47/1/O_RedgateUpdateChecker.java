

@CustomLog
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ExtensionMethod(StringUtils.class)
public class RedgateUpdateChecker {

    public static boolean isEnabled() {
        return usageChecker("flyway-cfu", VersionPrinter.getVersion());
    }

    
}