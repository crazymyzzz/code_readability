
@@ -77,12 +78,15 @@ public class FlywayPermit implements Serializable {
 
 
 
-    public FlywayPermit(String owner, Date permitExpiry, Date contractExpiry, boolean trial, boolean redgateEmployee) {
+
+
+    public FlywayPermit(String owner, Date permitExpiry, Date contractExpiry, boolean trial, boolean redgateEmployee, boolean fromAuth) {
         this.owner = owner;
         this.permitExpiry = permitExpiry;
         this.contractExpiry = contractExpiry;
         this.trial = trial;
         this.redgateEmployee = redgateEmployee;
+        this.fromAuth = fromAuth;
     }
 
     public void print() {
@@ -91,15 +95,18 @@ public class FlywayPermit implements Serializable {
         } else {
             LOG.info("Flyway " + this.tier.getDisplayName() + " Edition " + VersionPrinter.getVersion() + " by Redgate");
         }
-        if ("Online User".equals(this.owner)) {
-            if (this.contractExpiry.getTime() == Long.MAX_VALUE) {
-                LOG.debug("License has no expiry date");
-            } else {
+
+        if (contractExpiry != null) {
+            if ("Online User".equals(this.owner)) {
+                if (this.contractExpiry.getTime() == Long.MAX_VALUE) {
+                    LOG.debug("License has no expiry date");
+                } else {
+                    logLicensedUntilIfWithinWindow();
+                }
+            } else if (!"Anonymous".equals(this.owner)) {
+                LOG.info("Licensed to " + this.owner);
                 logLicensedUntilIfWithinWindow();
             }
-        } else if (!"Anonymous".equals(this.owner)) {
-            LOG.info("Licensed to " + this.owner);
-            logLicensedUntilIfWithinWindow();
         }
 
         if (!REFRESH_TOKEN_FILE.exists() && PERMIT_FILE.exists()) {
@@ -111,7 +118,7 @@ public class FlywayPermit implements Serializable {
         }
 
         if (this.tier == Tier.COMMUNITY && PERMIT_FILE.exists()) {
-            LOG.error("No Flyway licenses detected. Flyway fell back to Community Edition. Please add a Flyway license to your account and rerun auth. Alternatively, you can run auth -logout to remove your unlicensed permit on disk");
+            LOG.info("No Flyway license detected for this user - using Community Edition. If you expected a Teams/Enterprise license then please add a Flyway license to your account and rerun auth. Alternatively, you can run auth -logout to remove your unlicensed permit on disk");
         }
 
         if (isTrial()) {
