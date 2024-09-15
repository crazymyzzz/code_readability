@@ -17,19 +17,30 @@ package io.jsonwebtoken.impl;
 
 import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.Clock;
+import io.jsonwebtoken.CompressionCodec;
 import io.jsonwebtoken.CompressionCodecResolver;
 import io.jsonwebtoken.JwtParser;
 import io.jsonwebtoken.JwtParserBuilder;
+import io.jsonwebtoken.Locator;
 import io.jsonwebtoken.SigningKeyResolver;
 import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
+import io.jsonwebtoken.impl.lang.Services;
+import io.jsonwebtoken.impl.security.ConstantKeyLocator;
 import io.jsonwebtoken.io.Decoder;
 import io.jsonwebtoken.io.Decoders;
 import io.jsonwebtoken.io.Deserializer;
 import io.jsonwebtoken.lang.Assert;
-import io.jsonwebtoken.impl.lang.Services;
+import io.jsonwebtoken.lang.Collections;
+import io.jsonwebtoken.security.AeadAlgorithm;
+import io.jsonwebtoken.security.KeyAlgorithm;
+import io.jsonwebtoken.security.Keys;
+import io.jsonwebtoken.security.SecureDigestAlgorithm;
 
 import java.security.Key;
+import java.security.Provider;
+import java.util.Collection;
 import java.util.Date;
+import java.util.LinkedHashSet;
 import java.util.Map;
 
 /**
@@ -41,32 +52,65 @@ public class DefaultJwtParserBuilder implements JwtParserBuilder {
 
     /**
      * To prevent overflow per <a href="https://github.com/jwtk/jjwt/issues/583">Issue 583</a>.
-     *
+     * <p>
      * Package-protected on purpose to allow use in backwards-compatible {@link DefaultJwtParser} implementation.
      * TODO: enable private modifier on these two variables when deleting DefaultJwtParser
      */
     static final long MAX_CLOCK_SKEW_MILLIS = Long.MAX_VALUE / MILLISECONDS_PER_SECOND;
     static final String MAX_CLOCK_SKEW_ILLEGAL_MSG = "Illegal allowedClockSkewMillis value: multiplying this " +
-        "value by 1000 to obtain the number of milliseconds would cause a numeric overflow.";
+            "value by 1000 to obtain the number of milliseconds would cause a numeric overflow.";
+
+    private Provider provider;
+
+    private boolean enableUnsecuredJws = false;
+
+    private boolean enableUnsecuredDecompression = false;
+
+    private Locator<? extends Key> keyLocator;
+
+    @SuppressWarnings("deprecation") //TODO: remove for 1.0
+    private SigningKeyResolver signingKeyResolver = null;
+
+    private Locator<CompressionCodec> compressionCodecLocator;
 
-    private byte[] keyBytes;
+    private final Collection<AeadAlgorithm> extraEncryptionAlgorithms = new LinkedHashSet<>();
 
-    private Key key;
+    private final Collection<KeyAlgorithm<?, ?>> extraKeyAlgorithms = new LinkedHashSet<>();
 
-    private SigningKeyResolver signingKeyResolver;
+    private final Collection<SecureDigestAlgorithm<?, ?>> extraDigestAlgorithms = new LinkedHashSet<>();
 
-    private CompressionCodecResolver compressionCodecResolver;
+    private final Collection<CompressionCodec> extraCompressionCodecs = new LinkedHashSet<>();
 
     private Decoder<String, byte[]> base64UrlDecoder = Decoders.BASE64URL;
 
     private Deserializer<Map<String, ?>> deserializer;
 
-    private Claims expectedClaims = new DefaultClaims();
+    private final Claims expectedClaims = new DefaultClaims();
 
     private Clock clock = DefaultClock.INSTANCE;
 
     private long allowedClockSkewMillis = 0;
 
+    private Key signatureVerificationKey;
+    private Key decryptionKey;
+
+    @Override
+    public JwtParserBuilder enableUnsecuredJws() {
+        this.enableUnsecuredJws = true;
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder enableUnsecuredDecompression() {
+        this.enableUnsecuredDecompression = true;
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder setProvider(Provider provider) {
+        this.provider = provider;
+        return this;
+    }
 
     @Override
     public JwtParserBuilder deserializeJsonWith(Deserializer<Map<String, ?>> deserializer) {
@@ -148,25 +192,63 @@ public class DefaultJwtParserBuilder implements JwtParserBuilder {
 
     @Override
     public JwtParserBuilder setSigningKey(byte[] key) {
-        Assert.notEmpty(key, "signing key cannot be null or empty.");
-        this.keyBytes = key;
-        return this;
+        Assert.notEmpty(key, "signature verification key cannot be null or empty.");
+        return setSigningKey(Keys.hmacShaKeyFor(key));
     }
 
     @Override
     public JwtParserBuilder setSigningKey(String base64EncodedSecretKey) {
-        Assert.hasText(base64EncodedSecretKey, "signing key cannot be null or empty.");
-        this.keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
+        Assert.hasText(base64EncodedSecretKey, "signature verification key cannot be null or empty.");
+        byte[] bytes = Decoders.BASE64.decode(base64EncodedSecretKey);
+        return setSigningKey(bytes);
+    }
+
+    @Override
+    public JwtParserBuilder setSigningKey(final Key key) {
+        return verifyWith(key);
+    }
+
+    @Override
+    public JwtParserBuilder verifyWith(Key key) {
+        this.signatureVerificationKey = Assert.notNull(key, "signature verification key cannot be null.");
         return this;
     }
 
     @Override
-    public JwtParserBuilder setSigningKey(Key key) {
-        Assert.notNull(key, "signing key cannot be null.");
-        this.key = key;
+    public JwtParserBuilder decryptWith(final Key key) {
+        this.decryptionKey = Assert.notNull(key, "decryption key cannot be null.");
         return this;
     }
 
+    @Override
+    public JwtParserBuilder addCompressionCodecs(Collection<? extends CompressionCodec> codecs) {
+        Assert.notEmpty(codecs, "Additional CompressionCodec collection cannot be null or empty.");
+        this.extraCompressionCodecs.addAll(codecs);
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder addEncryptionAlgorithms(Collection<? extends AeadAlgorithm> encAlgs) {
+        Assert.notEmpty(encAlgs, "Additional AeadAlgorithm collection cannot be null or empty.");
+        this.extraEncryptionAlgorithms.addAll(encAlgs);
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder addSignatureAlgorithms(Collection<? extends SecureDigestAlgorithm<?, ?>> sigAlgs) {
+        Assert.notEmpty(sigAlgs, "Additional SignatureAlgorithm collection cannot be null or empty.");
+        this.extraDigestAlgorithms.addAll(sigAlgs);
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder addKeyAlgorithms(Collection<? extends KeyAlgorithm<?, ?>> keyAlgs) {
+        Assert.notEmpty(keyAlgs, "Additional KeyAlgorithm collection cannot be null or empty.");
+        this.extraKeyAlgorithms.addAll(keyAlgs);
+        return this;
+    }
+
+    @SuppressWarnings("deprecation") //TODO: remove for 1.0
     @Override
     public JwtParserBuilder setSigningKeyResolver(SigningKeyResolver signingKeyResolver) {
         Assert.notNull(signingKeyResolver, "SigningKeyResolver cannot be null.");
@@ -175,9 +257,21 @@ public class DefaultJwtParserBuilder implements JwtParserBuilder {
     }
 
     @Override
-    public JwtParserBuilder setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver) {
-        Assert.notNull(compressionCodecResolver, "compressionCodecResolver cannot be null.");
-        this.compressionCodecResolver = compressionCodecResolver;
+    public JwtParserBuilder setKeyLocator(Locator<Key> keyLocator) {
+        this.keyLocator = Assert.notNull(keyLocator, "Key locator cannot be null.");
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder setCompressionCodecLocator(Locator<CompressionCodec> locator) {
+        this.compressionCodecLocator = Assert.notNull(locator, "CompressionCodec locator cannot be null.");
+        return this;
+    }
+
+    @Override
+    public JwtParserBuilder setCompressionCodecResolver(CompressionCodecResolver resolver) {
+        Assert.notNull(resolver, "compressionCodecResolver cannot be null.");
+        this.compressionCodecLocator = new CompressionCodecLocator(resolver);
         return this;
     }
 
@@ -188,23 +282,58 @@ public class DefaultJwtParserBuilder implements JwtParserBuilder {
         // that is NOT exposed as a service and no other implementations are available for lookup.
         if (this.deserializer == null) {
             // try to find one based on the services available:
+            //noinspection unchecked
             this.deserializer = Services.loadFirst(Deserializer.class);
         }
 
-        // if the compressionCodecResolver is not set default it.
-        if (this.compressionCodecResolver == null) {
-            this.compressionCodecResolver = new DefaultCompressionCodecResolver();
+        if (this.signingKeyResolver != null && this.signatureVerificationKey != null) {
+            String msg = "Both 'signingKeyResolver and 'verifyWith/signWith' key cannot be configured. " +
+                    "Choose either, or prefer `keyLocator` when possible.";
+            throw new IllegalStateException(msg);
+        }
+        if (this.keyLocator != null && this.decryptionKey != null) {
+            String msg = "Both 'keyLocator' and 'decryptWith' key cannot be configured. Prefer 'keyLocator' if possible.";
+            throw new IllegalStateException(msg);
+        }
+        if (this.keyLocator == null) {
+            this.keyLocator = new ConstantKeyLocator(this.signatureVerificationKey, this.decryptionKey);
+        }
+
+        if (!enableUnsecuredJws && enableUnsecuredDecompression) {
+            String msg = "'enableUnsecuredDecompression' is only relevant if 'enableUnsecuredJws' is also " +
+                    "configured. Please read the JavaDoc of both features before enabling either " +
+                    "due to their security implications.";
+            throw new IllegalStateException(msg);
+        }
+        if (this.compressionCodecLocator != null && !Collections.isEmpty(extraCompressionCodecs)) {
+            String msg = "Both 'addCompressionCodecs' and 'compressionCodecLocator' " +
+                    "(or 'compressionCodecResolver') cannot be specified. Choose either.";
+            throw new IllegalStateException(msg);
+        }
+        if (this.compressionCodecLocator == null) {
+            this.compressionCodecLocator = new DefaultCompressionCodecResolver(extraCompressionCodecs);
         }
 
-        return new ImmutableJwtParser(
-                new DefaultJwtParser(signingKeyResolver,
-                                     key,
-                                     keyBytes,
-                                     clock,
-                                     allowedClockSkewMillis,
-                                     expectedClaims,
-                                     base64UrlDecoder,
-                                     new JwtDeserializer<>(deserializer),
-                                     compressionCodecResolver));
+        // Invariants.  If these are ever violated, it's an error in this class implementation
+        // (we default to non-null instances, and the setters should never allow null):
+        Assert.stateNotNull(this.keyLocator, "Key locator should never be null.");
+        Assert.stateNotNull(this.compressionCodecLocator, "CompressionCodec Locator should never be null.");
+
+        return new ImmutableJwtParser(new DefaultJwtParser(
+                provider,
+                signingKeyResolver,
+                enableUnsecuredJws,
+                enableUnsecuredDecompression,
+                keyLocator,
+                clock,
+                allowedClockSkewMillis,
+                expectedClaims,
+                base64UrlDecoder,
+                new JwtDeserializer<>(deserializer),
+                compressionCodecLocator,
+                extraDigestAlgorithms,
+                extraKeyAlgorithms,
+                extraEncryptionAlgorithms
+        ));
     }
 }
