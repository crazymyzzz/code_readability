@@ -39,6 +39,9 @@ public class LicenseGuard {
 
 
 
+     private static final FlywayPermit OSS_PERMIT = new FlywayPermit("Anonymous", null, null, false, false, false);
+
+
     public static void guard(Configuration configuration, List<Tier> editions, String featureName) {
         FlywayPermit flywayPermit = getPermit(configuration);
         if ((flywayPermit.getPermitExpiry() != null && flywayPermit.getPermitExpiry().before(new Date())) ||
@@ -122,7 +125,7 @@ public class LicenseGuard {
 
 
 
-         return new FlywayPermit("Anonymous", null, null, false, false);
+         return OSS_PERMIT;
 
     }
 
