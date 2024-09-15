@@ -15,16 +15,13 @@
  */
 package io.jsonwebtoken.security;
 
-import io.jsonwebtoken.SignatureAlgorithm;
+import io.jsonwebtoken.Jwts;
 import io.jsonwebtoken.lang.Assert;
 import io.jsonwebtoken.lang.Classes;
 
 import javax.crypto.SecretKey;
 import javax.crypto.spec.SecretKeySpec;
 import java.security.KeyPair;
-import java.util.Arrays;
-import java.util.Collections;
-import java.util.List;
 
 /**
  * Utility class for securely generating {@link SecretKey}s and {@link KeyPair}s.
@@ -33,36 +30,14 @@ import java.util.List;
  */
 public final class Keys {
 
-    private static final String MAC = "io.jsonwebtoken.impl.crypto.MacProvider";
-    private static final String RSA = "io.jsonwebtoken.impl.crypto.RsaProvider";
-    private static final String EC = "io.jsonwebtoken.impl.crypto.EllipticCurveProvider";
-
-    private static final Class[] SIG_ARG_TYPES = new Class[]{SignatureAlgorithm.class};
-
-    //purposefully ordered higher to lower:
-    private static final List<SignatureAlgorithm> PREFERRED_HMAC_ALGS = Collections.unmodifiableList(Arrays.asList(
-        SignatureAlgorithm.HS512, SignatureAlgorithm.HS384, SignatureAlgorithm.HS256));
+    private static final String BRIDGE_CLASSNAME = "io.jsonwebtoken.impl.security.KeysBridge";
+    private static final Class<?> BRIDGE_CLASS = Classes.forName(BRIDGE_CLASSNAME);
+    private static final Class<?>[] FOR_PASSWORD_ARG_TYPES = new Class[]{char[].class};
 
     //prevent instantiation
     private Keys() {
     }
 
-    /*
-    public static final int bitLength(Key key) throws IllegalArgumentException {
-        Assert.notNull(key, "Key cannot be null.");
-        if (key instanceof SecretKey) {
-            byte[] encoded = key.getEncoded();
-            return Arrays.length(encoded) * 8;
-        } else if (key instanceof RSAKey) {
-            return ((RSAKey)key).getModulus().bitLength();
-        } else if (key instanceof ECKey) {
-            return ((ECKey)key).getParams().getOrder().bitLength();
-        }
-
-        throw new IllegalArgumentException("Unsupported key type: " + key.getClass().getName());
-    }
-    */
-
     /**
      * Creates a new SecretKey instance for use with HMAC-SHA algorithms based on the specified key byte array.
      *
@@ -80,24 +55,45 @@ public final class Keys {
 
         int bitLength = bytes.length * 8;
 
-        for (SignatureAlgorithm alg : PREFERRED_HMAC_ALGS) {
-            if (bitLength >= alg.getMinKeyLength()) {
-                return new SecretKeySpec(bytes, alg.getJcaName());
-            }
+        //Purposefully ordered higher to lower to ensure the strongest key possible can be generated.
+        if (bitLength >= 512) {
+            return new SecretKeySpec(bytes, "HmacSHA512");
+        } else if (bitLength >= 384) {
+            return new SecretKeySpec(bytes, "HmacSHA384");
+        } else if (bitLength >= 256) {
+            return new SecretKeySpec(bytes, "HmacSHA256");
         }
 
         String msg = "The specified key byte array is " + bitLength + " bits which " +
-            "is not secure enough for any JWT HMAC-SHA algorithm.  The JWT " +
-            "JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a " +
-            "size >= 256 bits (the key size must be greater than or equal to the hash " +
-            "output size).  Consider using the " + Keys.class.getName() + "#secretKeyFor(SignatureAlgorithm) method " +
-            "to create a key guaranteed to be secure enough for your preferred HMAC-SHA algorithm.  See " +
-            "https://tools.ietf.org/html/rfc7518#section-3.2 for more information.";
+                "is not secure enough for any JWT HMAC-SHA algorithm.  The JWT " +
+                "JWA Specification (RFC 7518, Section 3.2) states that keys used with HMAC-SHA algorithms MUST have a " +
+                "size >= 256 bits (the key size must be greater than or equal to the hash " +
+                "output size).  Consider using the StandardSecureDigestAlgorithms.HS256.keyBuilder() method (or HS384.keyBuilder() " +
+                "or HS512.keyBuilder()) to create a key guaranteed to be secure enough for your preferred HMAC-SHA " +
+                "algorithm.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more information.";
         throw new WeakKeyException(msg);
     }
 
     /**
-     * Returns a new {@link SecretKey} with a key length suitable for use with the specified {@link SignatureAlgorithm}.
+     * <p><b>Deprecation Notice</b></p>
+     *
+     * <p>As of JJWT JJWT_RELEASE_VERSION, symmetric (secret) key algorithm instances can generate a key of suitable
+     * length for that specific algorithm by calling their {@code keyBuilder()} method directly. For example:</p>
+     *
+     * <pre><code>
+     * {@link StandardSecureDigestAlgorithms#HS256}.keyBuilder().build();
+     * {@link StandardSecureDigestAlgorithms#HS384}.keyBuilder().build();
+     * {@link StandardSecureDigestAlgorithms#HS512}.keyBuilder().build();
+     * </code></pre>
+     *
+     * <p>Call those methods as needed instead of this static {@code secretKeyFor} helper method - the returned
+     * {@link KeyBuilder} allows callers to specify a preferred Provider or SecureRandom on the builder if
+     * desired, whereas this {@code secretKeyFor} method does not. Consequently this helper method will be removed
+     * before the 1.0 release.</p>
+     *
+     * <p><b>Previous Documentation</b></p>
+     *
+     * <p>Returns a new {@link SecretKey} with a key length suitable for use with the specified {@link SignatureAlgorithm}.</p>
      *
      * <p><a href="https://tools.ietf.org/html/rfc7518#section-3.2">JWA Specification (RFC 7518), Section 3.2</a>
      * requires minimum key lengths to be used for each respective Signature Algorithm.  This method returns a
@@ -125,24 +121,44 @@ public final class Keys {
      *
      * @param alg the {@code SignatureAlgorithm} to inspect to determine which key length to use.
      * @return a new {@link SecretKey} instance suitable for use with the specified {@link SignatureAlgorithm}.
-     * @throws IllegalArgumentException for any input value other than {@link SignatureAlgorithm#HS256},
-     *                                  {@link SignatureAlgorithm#HS384}, or {@link SignatureAlgorithm#HS512}
+     * @throws IllegalArgumentException for any input value other than {@link io.jsonwebtoken.SignatureAlgorithm#HS256},
+     *                                  {@link io.jsonwebtoken.SignatureAlgorithm#HS384}, or {@link io.jsonwebtoken.SignatureAlgorithm#HS512}
+     * @deprecated since JJWT_RELEASE_VERSION.  Use your preferred {@link MacAlgorithm} instance's
+     * {@link MacAlgorithm#keyBuilder() keyBuilder()} method directly.
      */
-    public static SecretKey secretKeyFor(SignatureAlgorithm alg) throws IllegalArgumentException {
+    @SuppressWarnings("DeprecatedIsStillUsed")
+    @Deprecated
+    public static SecretKey secretKeyFor(io.jsonwebtoken.SignatureAlgorithm alg) throws IllegalArgumentException {
         Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
-        switch (alg) {
-            case HS256:
-            case HS384:
-            case HS512:
-                return Classes.invokeStatic(MAC, "generateKey", SIG_ARG_TYPES, alg);
-            default:
-                String msg = "The " + alg.name() + " algorithm does not support shared secret keys.";
-                throw new IllegalArgumentException(msg);
+        SecureDigestAlgorithm<?, ?> salg = Jwts.SIG.get(alg.name());
+        if (!(salg instanceof MacAlgorithm)) {
+            String msg = "The " + alg.name() + " algorithm does not support shared secret keys.";
+            throw new IllegalArgumentException(msg);
         }
+        return ((MacAlgorithm) salg).keyBuilder().build();
     }
 
     /**
-     * Returns a new {@link KeyPair} suitable for use with the specified asymmetric algorithm.
+     * <p><b>Deprecation Notice</b></p>
+     *
+     * <p>As of JJWT JJWT_RELEASE_VERSION, asymmetric key algorithm instances can generate KeyPairs of suitable strength
+     * for that specific algorithm by calling their {@code keyPairBuilder()} method directly. For example:</p>
+     *
+     * <blockquote><pre>
+     * Jwts.SIG.{@link StandardSecureDigestAlgorithms#RS256 RS256}.keyPairBuilder().build();
+     * Jwts.SIG.{@link StandardSecureDigestAlgorithms#RS384 RS384}.keyPairBuilder().build();
+     * Jwts.SIG.{@link StandardSecureDigestAlgorithms#RS512 RS512}.keyPairBuilder().build();
+     * ... etc ...
+     * Jwts.SIG.{@link StandardSecureDigestAlgorithms#ES512 ES512}.keyPairBuilder().build();</pre></blockquote>
+     *
+     * <p>Call those methods as needed instead of this static {@code keyPairFor} helper method - the returned
+     * {@link KeyPairBuilder} allows callers to specify a preferred Provider or SecureRandom on the builder if
+     * desired, whereas this {@code keyPairFor} method does not. Consequently this helper method will be removed
+     * before the 1.0 release.</p>
+     *
+     * <p><b>Previous Documentation</b></p>
+     *
+     * <p>Returns a new {@link KeyPair} suitable for use with the specified asymmetric algorithm.</p>
      *
      * <p>If the {@code alg} argument is an RSA algorithm, a KeyPair is generated based on the following:</p>
      *
@@ -202,7 +218,7 @@ public final class Keys {
      * </tr>
      * <tr>
      * <td>EC512</td>
-     * <td>512 bits</td>
+     * <td><b>521</b> bits</td>
      * <td>{@code P-521}</td>
      * <td>{@code secp521r1}</td>
      * </tr>
@@ -211,24 +227,44 @@ public final class Keys {
      * @param alg the {@code SignatureAlgorithm} to inspect to determine which asymmetric algorithm to use.
      * @return a new {@link KeyPair} suitable for use with the specified asymmetric algorithm.
      * @throws IllegalArgumentException if {@code alg} is not an asymmetric algorithm
+     * @deprecated since JJWT_RELEASE_VERSION in favor of your preferred
+     * {@link io.jsonwebtoken.security.SignatureAlgorithm} instance's
+     * {@link SignatureAlgorithm#keyPairBuilder() keyPairBuilder()} method directly.
      */
-    public static KeyPair keyPairFor(SignatureAlgorithm alg) throws IllegalArgumentException {
+    @SuppressWarnings("DeprecatedIsStillUsed")
+    @Deprecated
+    public static KeyPair keyPairFor(io.jsonwebtoken.SignatureAlgorithm alg) throws IllegalArgumentException {
         Assert.notNull(alg, "SignatureAlgorithm cannot be null.");
-        switch (alg) {
-            case RS256:
-            case PS256:
-            case RS384:
-            case PS384:
-            case RS512:
-            case PS512:
-                return Classes.invokeStatic(RSA, "generateKeyPair", SIG_ARG_TYPES, alg);
-            case ES256:
-            case ES384:
-            case ES512:
-                return Classes.invokeStatic(EC, "generateKeyPair", SIG_ARG_TYPES, alg);
-            default:
-                String msg = "The " + alg.name() + " algorithm does not support Key Pairs.";
-                throw new IllegalArgumentException(msg);
+        SecureDigestAlgorithm<?, ?> salg = Jwts.SIG.get(alg.name());
+        if (!(salg instanceof SignatureAlgorithm)) {
+            String msg = "The " + alg.name() + " algorithm does not support Key Pairs.";
+            throw new IllegalArgumentException(msg);
         }
+        SignatureAlgorithm asalg = ((SignatureAlgorithm) salg);
+        return asalg.keyPairBuilder().build();
+    }
+
+    /**
+     * Returns a new {@link Password} instance suitable for use with password-based key derivation algorithms.
+     *
+     * <p><b>Usage Note</b>: Using {@code Password}s outside of key derivation contexts will likely
+     * fail. See the {@link Password} JavaDoc for more, and also note the <b>Password Safety</b> section below.</p>
+     *
+     * <p><b>Password Safety</b></p>
+     *
+     * <p>Instances returned by this method use a <em>clone</em> of the specified {@code password} character array
+     * argument - changes to the argument array will NOT be reflected in the returned key, and vice versa.  If you wish
+     * to clear a {@code Password} instance to ensure it is no longer usable, call its {@link Password#destroy()}
+     * method will clear/overwrite its internal cloned char array. Also note that each subsequent call to
+     * {@link Password#toCharArray()} will also return a new clone of the underlying password character array per
+     * standard JCE key behavior.</p>
+     *
+     * @param password the raw password character array to clone for use with password-based key derivation algorithms.
+     * @return a new {@link Password} instance that wraps a new clone of the specified {@code password} character array.
+     * @see Password#toCharArray()
+     * @since JJWT_RELEASE_VERSION
+     */
+    public static Password forPassword(char[] password) {
+        return Classes.invokeStatic(BRIDGE_CLASS, "forPassword", FOR_PASSWORD_ARG_TYPES, new Object[]{password});
     }
 }
