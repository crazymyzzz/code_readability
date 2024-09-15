@@ -19,16 +19,36 @@ import java.security.Provider;
 import java.security.Security;
 import java.util.concurrent.atomic.AtomicBoolean;
 
+/**
+ * No longer used by JJWT.  Will be removed before the 1.0 final release.
+ *
+ * @deprecated since JJWT_RELEASE_VERSION. will be removed before the 1.0 final release.
+ */
+@Deprecated
 public final class RuntimeEnvironment {
 
-    private RuntimeEnvironment(){} //prevent instantiation
+    private RuntimeEnvironment() {
+    } //prevent instantiation
 
     private static final String BC_PROVIDER_CLASS_NAME = "org.bouncycastle.jce.provider.BouncyCastleProvider";
 
     private static final AtomicBoolean bcLoaded = new AtomicBoolean(false);
 
+    /**
+     * {@code true} if BouncyCastle is in the runtime classpath, {@code false} otherwise.
+     *
+     * @deprecated since JJWT_RELEASE_VERSION. will be removed before the 1.0 final release.
+     */
+    @Deprecated
     public static final boolean BOUNCY_CASTLE_AVAILABLE = Classes.isAvailable(BC_PROVIDER_CLASS_NAME);
 
+    /**
+     * Register BouncyCastle as a JCA provider in the system's {@link Security#getProviders() Security Providers} list
+     * if BouncyCastle is in the runtime classpath.
+     *
+     * @deprecated since JJWT_RELEASE_VERSION. will be removed before the 1.0 final release.
+     */
+    @Deprecated
     public static void enableBouncyCastleIfPossible() {
 
         if (!BOUNCY_CASTLE_AVAILABLE || bcLoaded.get()) {
@@ -36,13 +56,13 @@ public final class RuntimeEnvironment {
         }
 
         try {
-            Class clazz = Classes.forName(BC_PROVIDER_CLASS_NAME);
+            Class<Provider> clazz = Classes.forName(BC_PROVIDER_CLASS_NAME);
 
             //check to see if the user has already registered the BC provider:
 
             Provider[] providers = Security.getProviders();
 
-            for(Provider provider : providers) {
+            for (Provider provider : providers) {
                 if (clazz.isInstance(provider)) {
                     bcLoaded.set(true);
                     return;
@@ -50,7 +70,8 @@ public final class RuntimeEnvironment {
             }
 
             //bc provider not enabled - add it:
-            Security.addProvider((Provider)Classes.newInstance(clazz));
+            Provider provider = Classes.newInstance(clazz);
+            Security.addProvider(provider);
             bcLoaded.set(true);
 
         } catch (UnknownClassException e) {
