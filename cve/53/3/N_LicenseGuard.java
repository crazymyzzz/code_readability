

@CustomLog
@ExtensionMethod(Tier.class)
public class LicenseGuard {






     private static final FlywayPermit OSS_PERMIT = new FlywayPermit("Anonymous", null, null, false, false, false);




    public static FlywayPermit getPermit(Configuration configuration, boolean fromCache) {

























         return OSS_PERMIT;

    }












    }
}