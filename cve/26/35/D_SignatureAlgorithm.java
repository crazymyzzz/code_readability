@@ -18,6 +18,7 @@ package io.jsonwebtoken;
 import io.jsonwebtoken.security.InvalidKeyException;
 import io.jsonwebtoken.security.Keys;
 import io.jsonwebtoken.security.SignatureException;
+import io.jsonwebtoken.security.StandardSecureDigestAlgorithms;
 import io.jsonwebtoken.security.WeakKeyException;
 
 import javax.crypto.SecretKey;
@@ -34,7 +35,9 @@ import java.util.List;
  * <a href="https://tools.ietf.org/html/draft-ietf-jose-json-web-algorithms-31">JSON Web Algorithms</a> specification.
  *
  * @since 0.1
+ * @deprecated since JJWT_RELEASE_VERSION; use {@link StandardSecureDigestAlgorithms} instead.
  */
+@Deprecated
 public enum SignatureAlgorithm {
 
     /**
@@ -110,10 +113,10 @@ public enum SignatureAlgorithm {
 
     //purposefully ordered higher to lower:
     private static final List<SignatureAlgorithm> PREFERRED_HMAC_ALGS = Collections.unmodifiableList(Arrays.asList(
-        SignatureAlgorithm.HS512, SignatureAlgorithm.HS384, SignatureAlgorithm.HS256));
+            SignatureAlgorithm.HS512, SignatureAlgorithm.HS384, SignatureAlgorithm.HS256));
     //purposefully ordered higher to lower:
     private static final List<SignatureAlgorithm> PREFERRED_EC_ALGS = Collections.unmodifiableList(Arrays.asList(
-        SignatureAlgorithm.ES512, SignatureAlgorithm.ES384, SignatureAlgorithm.ES256));
+            SignatureAlgorithm.ES512, SignatureAlgorithm.ES384, SignatureAlgorithm.ES256));
 
     private final String value;
     private final String description;
@@ -132,7 +135,7 @@ public enum SignatureAlgorithm {
 
     SignatureAlgorithm(String value, String description, String familyName, String jcaName, boolean jdkStandard,
                        int digestLength, int minKeyLength) {
-        this(value, description,familyName, jcaName, jdkStandard, digestLength, minKeyLength, jcaName);
+        this(value, description, familyName, jcaName, jdkStandard, digestLength, minKeyLength, jcaName);
     }
 
     SignatureAlgorithm(String value, String description, String familyName, String jcaName, boolean jdkStandard,
@@ -365,25 +368,25 @@ public enum SignatureAlgorithm {
 
             // These next checks use equalsIgnoreCase per https://github.com/jwtk/jjwt/issues/381#issuecomment-412912272
             if (!HS256.jcaName.equalsIgnoreCase(alg) &&
-                !HS384.jcaName.equalsIgnoreCase(alg) &&
-                !HS512.jcaName.equalsIgnoreCase(alg) &&
-                !HS256.pkcs12Name.equals(alg) &&
-                !HS384.pkcs12Name.equals(alg) &&
-                !HS512.pkcs12Name.equals(alg)) {
+                    !HS384.jcaName.equalsIgnoreCase(alg) &&
+                    !HS512.jcaName.equalsIgnoreCase(alg) &&
+                    !HS256.pkcs12Name.equals(alg) &&
+                    !HS384.pkcs12Name.equals(alg) &&
+                    !HS512.pkcs12Name.equals(alg)) {
                 throw new InvalidKeyException("The " + keyType(signing) + " key's algorithm '" + alg +
-                    "' does not equal a valid HmacSHA* algorithm name and cannot be used with " + name() + ".");
+                        "' does not equal a valid HmacSHA* algorithm name and cannot be used with " + name() + ".");
             }
 
             int size = encoded.length * 8; //size in bits
             if (size < this.minKeyLength) {
                 String msg = "The " + keyType(signing) + " key's size is " + size + " bits which " +
-                    "is not secure enough for the " + name() + " algorithm.  The JWT " +
-                    "JWA Specification (RFC 7518, Section 3.2) states that keys used with " + name() + " MUST have a " +
-                    "size >= " + minKeyLength + " bits (the key size must be greater than or equal to the hash " +
-                    "output size).  Consider using the " + Keys.class.getName() + " class's " +
-                    "'secretKeyFor(SignatureAlgorithm." + name() + ")' method to create a key guaranteed to be " +
-                    "secure enough for " + name() + ".  See " +
-                    "https://tools.ietf.org/html/rfc7518#section-3.2 for more information.";
+                        "is not secure enough for the " + name() + " algorithm.  The JWT " +
+                        "JWA Specification (RFC 7518, Section 3.2) states that keys used with " + name() + " MUST have a " +
+                        "size >= " + minKeyLength + " bits (the key size must be greater than or equal to the hash " +
+                        "output size).  Consider using the " + Keys.class.getName() + " class's " +
+                        "'secretKeyFor(SignatureAlgorithm." + name() + ")' method to create a key guaranteed to be " +
+                        "secure enough for " + name() + ".  See " +
+                        "https://tools.ietf.org/html/rfc7518#section-3.2 for more information.";
                 throw new WeakKeyException(msg);
             }
 
@@ -407,13 +410,13 @@ public enum SignatureAlgorithm {
                 int size = ecKey.getParams().getOrder().bitLength();
                 if (size < this.minKeyLength) {
                     String msg = "The " + keyType(signing) + " key's size (ECParameterSpec order) is " + size +
-                        " bits which is not secure enough for the " + name() + " algorithm.  The JWT " +
-                        "JWA Specification (RFC 7518, Section 3.4) states that keys used with " +
-                        name() + " MUST have a size >= " + this.minKeyLength +
-                        " bits.  Consider using the " + Keys.class.getName() + " class's " +
-                        "'keyPairFor(SignatureAlgorithm." + name() + ")' method to create a key pair guaranteed " +
-                        "to be secure enough for " + name() + ".  See " +
-                        "https://tools.ietf.org/html/rfc7518#section-3.4 for more information.";
+                            " bits which is not secure enough for the " + name() + " algorithm.  The JWT " +
+                            "JWA Specification (RFC 7518, Section 3.4) states that keys used with " +
+                            name() + " MUST have a size >= " + this.minKeyLength +
+                            " bits.  Consider using the " + Keys.class.getName() + " class's " +
+                            "'keyPairFor(SignatureAlgorithm." + name() + ")' method to create a key pair guaranteed " +
+                            "to be secure enough for " + name() + ".  See " +
+                            "https://tools.ietf.org/html/rfc7518#section-3.4 for more information.";
                     throw new WeakKeyException(msg);
                 }
 
@@ -431,12 +434,12 @@ public enum SignatureAlgorithm {
                     String section = name().startsWith("P") ? "3.5" : "3.3";
 
                     String msg = "The " + keyType(signing) + " key's size is " + size + " bits which is not secure " +
-                        "enough for the " + name() + " algorithm.  The JWT JWA Specification (RFC 7518, Section " +
-                        section + ") states that keys used with " + name() + " MUST have a size >= " +
-                        this.minKeyLength + " bits.  Consider using the " + Keys.class.getName() + " class's " +
-                        "'keyPairFor(SignatureAlgorithm." + name() + ")' method to create a key pair guaranteed " +
-                        "to be secure enough for " + name() + ".  See " +
-                        "https://tools.ietf.org/html/rfc7518#section-" + section + " for more information.";
+                            "enough for the " + name() + " algorithm.  The JWT JWA Specification (RFC 7518, Section " +
+                            section + ") states that keys used with " + name() + " MUST have a size >= " +
+                            this.minKeyLength + " bits.  Consider using the " + Keys.class.getName() + " class's " +
+                            "'keyPairFor(SignatureAlgorithm." + name() + ")' method to create a key pair guaranteed " +
+                            "to be secure enough for " + name() + ".  See " +
+                            "https://tools.ietf.org/html/rfc7518#section-" + section + " for more information.";
                     throw new WeakKeyException(msg);
                 }
             }
@@ -563,19 +566,19 @@ public enum SignatureAlgorithm {
         }
 
         if (!(key instanceof SecretKey ||
-            (key instanceof PrivateKey && (key instanceof ECKey || key instanceof RSAKey)))) {
+                (key instanceof PrivateKey && (key instanceof ECKey || key instanceof RSAKey)))) {
             String msg = "JWT standard signing algorithms require either 1) a SecretKey for HMAC-SHA algorithms or " +
-                "2) a private RSAKey for RSA algorithms or 3) a private ECKey for Elliptic Curve algorithms.  " +
-                "The specified key is of type " + key.getClass().getName();
+                    "2) a private RSAKey for RSA algorithms or 3) a private ECKey for Elliptic Curve algorithms.  " +
+                    "The specified key is of type " + key.getClass().getName();
             throw new InvalidKeyException(msg);
         }
 
         if (key instanceof SecretKey) {
 
-            SecretKey secretKey = (SecretKey)key;
+            SecretKey secretKey = (SecretKey) key;
             int bitLength = io.jsonwebtoken.lang.Arrays.length(secretKey.getEncoded()) * Byte.SIZE;
 
-            for(SignatureAlgorithm alg : PREFERRED_HMAC_ALGS) {
+            for (SignatureAlgorithm alg : PREFERRED_HMAC_ALGS) {
                 // ensure compatibility check is based on key length. See https://github.com/jwtk/jjwt/issues/381
                 if (bitLength >= alg.minKeyLength) {
                     return alg;
@@ -583,9 +586,9 @@ public enum SignatureAlgorithm {
             }
 
             String msg = "The specified SecretKey is not strong enough to be used with JWT HMAC signature " +
-                "algorithms.  The JWT specification requires HMAC keys to be >= 256 bits long.  The specified " +
-                "key is " + bitLength + " bits.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more " +
-                "information.";
+                    "algorithms.  The JWT specification requires HMAC keys to be >= 256 bits long.  The specified " +
+                    "key is " + bitLength + " bits.  See https://tools.ietf.org/html/rfc7518#section-3.2 for more " +
+                    "information.";
             throw new WeakKeyException(msg);
         }
 
@@ -606,9 +609,9 @@ public enum SignatureAlgorithm {
             }
 
             String msg = "The specified RSA signing key is not strong enough to be used with JWT RSA signature " +
-                "algorithms.  The JWT specification requires RSA keys to be >= 2048 bits long.  The specified RSA " +
-                "key is " + bitLength + " bits.  See https://tools.ietf.org/html/rfc7518#section-3.3 for more " +
-                "information.";
+                    "algorithms.  The JWT specification requires RSA keys to be >= 2048 bits long.  The specified RSA " +
+                    "key is " + bitLength + " bits.  See https://tools.ietf.org/html/rfc7518#section-3.3 for more " +
+                    "information.";
             throw new WeakKeyException(msg);
         }
 
@@ -626,9 +629,9 @@ public enum SignatureAlgorithm {
         }
 
         String msg = "The specified Elliptic Curve signing key is not strong enough to be used with JWT ECDSA " +
-            "signature algorithms.  The JWT specification requires ECDSA keys to be >= 256 bits long.  " +
-            "The specified ECDSA key is " + bitLength + " bits.  See " +
-            "https://tools.ietf.org/html/rfc7518#section-3.4 for more information.";
+                "signature algorithms.  The JWT specification requires ECDSA keys to be >= 256 bits long.  " +
+                "The specified ECDSA key is " + bitLength + " bits.  See " +
+                "https://tools.ietf.org/html/rfc7518#section-3.4 for more information.";
         throw new WeakKeyException(msg);
     }
 
