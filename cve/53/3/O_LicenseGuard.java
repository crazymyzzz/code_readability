
@CustomLog
@ExtensionMethod(Tier.class)
public class LicenseGuard {



    public static FlywayPermit getPermit(Configuration configuration, boolean fromCache) {

























         return new FlywayPermit("Anonymous", null, null, false, false);

    }


}