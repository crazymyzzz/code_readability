@@ -41,6 +41,7 @@ public class RootTelemetryModel {
     private boolean isTrial;
     private boolean isSignedIn;
     private String containerType;
+    private String secretsManagementType;
 
     private Instant startTime = Instant.now();
 }
