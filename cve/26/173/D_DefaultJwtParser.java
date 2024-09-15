@@ -15,60 +15,170 @@
  */
 package io.jsonwebtoken.impl;
 
-import io.jsonwebtoken.ClaimJwtException;
 import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.Clock;
 import io.jsonwebtoken.CompressionCodec;
 import io.jsonwebtoken.CompressionCodecResolver;
 import io.jsonwebtoken.ExpiredJwtException;
 import io.jsonwebtoken.Header;
+import io.jsonwebtoken.Identifiable;
 import io.jsonwebtoken.IncorrectClaimException;
-import io.jsonwebtoken.InvalidClaimException;
+import io.jsonwebtoken.Jwe;
+import io.jsonwebtoken.JweHeader;
 import io.jsonwebtoken.Jws;
 import io.jsonwebtoken.JwsHeader;
 import io.jsonwebtoken.Jwt;
+import io.jsonwebtoken.JwtException;
 import io.jsonwebtoken.JwtHandler;
 import io.jsonwebtoken.JwtHandlerAdapter;
 import io.jsonwebtoken.JwtParser;
+import io.jsonwebtoken.Jwts;
+import io.jsonwebtoken.Locator;
 import io.jsonwebtoken.MalformedJwtException;
 import io.jsonwebtoken.MissingClaimException;
 import io.jsonwebtoken.PrematureJwtException;
-import io.jsonwebtoken.SignatureAlgorithm;
 import io.jsonwebtoken.SigningKeyResolver;
+import io.jsonwebtoken.UnprotectedHeader;
 import io.jsonwebtoken.UnsupportedJwtException;
 import io.jsonwebtoken.impl.compression.DefaultCompressionCodecResolver;
-import io.jsonwebtoken.impl.crypto.DefaultJwtSignatureValidator;
-import io.jsonwebtoken.impl.crypto.JwtSignatureValidator;
+import io.jsonwebtoken.impl.lang.Bytes;
+import io.jsonwebtoken.impl.lang.Function;
+import io.jsonwebtoken.impl.lang.IdRegistry;
 import io.jsonwebtoken.impl.lang.LegacyServices;
+import io.jsonwebtoken.impl.security.ConstantKeyLocator;
+import io.jsonwebtoken.impl.security.DefaultAeadResult;
+import io.jsonwebtoken.impl.security.DefaultDecryptionKeyRequest;
+import io.jsonwebtoken.impl.security.DefaultVerifySecureDigestRequest;
+import io.jsonwebtoken.impl.security.LocatingKeyResolver;
 import io.jsonwebtoken.io.Decoder;
 import io.jsonwebtoken.io.Decoders;
+import io.jsonwebtoken.io.DecodingException;
+import io.jsonwebtoken.io.DeserializationException;
 import io.jsonwebtoken.io.Deserializer;
+import io.jsonwebtoken.lang.Arrays;
 import io.jsonwebtoken.lang.Assert;
+import io.jsonwebtoken.lang.Collections;
 import io.jsonwebtoken.lang.DateFormats;
-import io.jsonwebtoken.lang.Objects;
 import io.jsonwebtoken.lang.Strings;
+import io.jsonwebtoken.security.AeadAlgorithm;
+import io.jsonwebtoken.security.DecryptAeadRequest;
+import io.jsonwebtoken.security.DecryptionKeyRequest;
 import io.jsonwebtoken.security.InvalidKeyException;
+import io.jsonwebtoken.security.KeyAlgorithm;
+import io.jsonwebtoken.security.Keys;
+import io.jsonwebtoken.security.Message;
+import io.jsonwebtoken.security.SecureDigestAlgorithm;
 import io.jsonwebtoken.security.SignatureException;
+import io.jsonwebtoken.security.VerifySecureDigestRequest;
 import io.jsonwebtoken.security.WeakKeyException;
 
-import javax.crypto.spec.SecretKeySpec;
+import javax.crypto.SecretKey;
+import java.nio.charset.StandardCharsets;
 import java.security.Key;
+import java.security.Provider;
+import java.util.Collection;
 import java.util.Date;
+import java.util.LinkedHashSet;
 import java.util.Map;
 
 @SuppressWarnings("unchecked")
 public class DefaultJwtParser implements JwtParser {
 
+    static final char SEPARATOR_CHAR = '.';
+
     private static final int MILLISECONDS_PER_SECOND = 1000;
 
-    // TODO: make the folling fields final for v1.0
-    private byte[] keyBytes;
+    private static final JwtTokenizer jwtTokenizer = new JwtTokenizer();
+
+    public static final String INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE = "Expected %s claim to be: %s, but was: %s.";
+    public static final String MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE = "Expected %s claim to be: %s, but was not " +
+            "present in the JWT claims.";
+
+    public static final String MISSING_JWS_ALG_MSG = "JWS header does not contain a required 'alg' (Algorithm) " +
+            "header parameter.  This header parameter is mandatory per the JWS Specification, Section 4.1.1. See " +
+            "https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.1 for more information.";
+
+    public static final String MISSING_JWE_ALG_MSG = "JWE header does not contain a required 'alg' (Algorithm) " +
+            "header parameter.  This header parameter is mandatory per the JWE Specification, Section 4.1.1. See " +
+            "https://www.rfc-editor.org/rfc/rfc7516.html#section-4.1.1 for more information.";
+
+    public static final String MISSING_JWS_DIGEST_MSG_FMT = "The JWS header references signature algorithm '%s' but " +
+            "the compact JWE string is missing the required signature.";
+
+    public static final String MISSING_JWE_DIGEST_MSG_FMT = "The JWE header references key management algorithm '%s' " +
+            "but the compact JWE string is missing the " + "required AAD authentication tag.";
+
+    private static final String MISSING_ENC_MSG = "JWE header does not contain a required " +
+            "'enc' (Encryption Algorithm) header parameter.  " + "This header parameter is mandatory per the JWE " +
+            "Specification, Section 4.1.2. See " +
+            "https://www.rfc-editor.org/rfc/rfc7516.html#section-4.1.2 for more information.";
+
+    private static final String UNSECURED_DISABLED_MSG_PREFIX = "Unsecured JWSs (those with an " +
+            AbstractHeader.ALGORITHM + " header value of '" + Jwts.SIG.NONE.getId() + "') are disallowed by " +
+            "default as mandated by https://www.rfc-editor.org/rfc/rfc7518.html#section-3.6. If you wish to " +
+            "allow them to be parsed, call the JwtParserBuilder.enableUnsecuredJws() method (but please read the " +
+            "security considerations covered in that method's JavaDoc before doing so). Header: ";
+
+    private static final String JWE_NONE_MSG = "JWEs do not support key management " + AbstractHeader.ALGORITHM +
+            " header value '" + Jwts.SIG.NONE.getId() + "' per " +
+            "https://www.rfc-editor.org/rfc/rfc7518.html#section-4.1";
+
+    private static final String JWS_NONE_SIG_MISMATCH_MSG = "The JWS header references signature algorithm '" +
+            Jwts.SIG.NONE.getId() + "' yet the compact JWS string contains a signature. This is not permitted " +
+            "per https://tools.ietf.org/html/rfc7518#section-3.6.";
+    private static final String UNPROTECTED_DECOMPRESSION_MSG = "The JWT header references compression algorithm " +
+            "'%s', but payload decompression for Unsecured JWTs (those with an " + AbstractHeader.ALGORITHM +
+            " header value of '" + Jwts.SIG.NONE.getId() + "') are " + "disallowed by default to protect " +
+            "against [Denial of Service attacks](" +
+            "https://www.usenix.org/system/files/conference/usenixsecurity15/sec15-paper-pellegrino.pdf).  If you " +
+            "wish to enable Unsecure JWS payload decompression, call the JwtParserBuilder." +
+            "enableUnsecuredDecompression() method (but please read the security considerations covered in that " +
+            "method's JavaDoc before doing so).";
+
+    private static <I extends Identifiable> IdRegistry<I> newRegistry(String name, Collection<I> defaults, Collection<I> extras) {
+        Collection<I> all = new LinkedHashSet<>(Collections.size(extras) + defaults.size());
+        all.addAll(extras);
+        all.addAll(defaults);
+        return new IdRegistry<>(name, all);
+    }
+
+    private static Function<JwsHeader, SecureDigestAlgorithm<?, ?>> sigFn(Collection<SecureDigestAlgorithm<?, ?>> extras) {
+        String name = "JWS MAC or Signature Algorithm";
+        IdRegistry<SecureDigestAlgorithm<?, ?>> registry = newRegistry(name, Jwts.SIG.values(), extras);
+        return new IdLocator<>(AbstractHeader.ALGORITHM, MISSING_JWS_ALG_MSG, registry);
+    }
 
-    private Key key;
+    private static Function<JweHeader, AeadAlgorithm> encFn(Collection<AeadAlgorithm> extras) {
+        String name = "JWE Encryption Algorithm";
+        IdRegistry<AeadAlgorithm> registry = newRegistry(name, Jwts.ENC.values(), extras);
+        return new IdLocator<>(DefaultJweHeader.ENCRYPTION_ALGORITHM, MISSING_ENC_MSG, registry);
+    }
 
+    private static Function<JweHeader, KeyAlgorithm<?, ?>> keyFn(Collection<KeyAlgorithm<?, ?>> extras) {
+        String name = "JWE Key Management Algorithm";
+        IdRegistry<KeyAlgorithm<?, ?>> registry = newRegistry(name, Jwts.KEY.values(), extras);
+        return new IdLocator<>(AbstractHeader.ALGORITHM, MISSING_JWE_ALG_MSG, registry);
+    }
+
+    // TODO: make the following fields final for v1.0
+    private Provider provider;
+
+    @SuppressWarnings("deprecation") // will remove for 1.0
     private SigningKeyResolver signingKeyResolver;
 
-    private CompressionCodecResolver compressionCodecResolver = new DefaultCompressionCodecResolver();
+    private Locator<CompressionCodec> compressionCodecLocator;
+
+    private final boolean enableUnsecuredJws;
+
+    private final boolean enableUnsecuredDecompression;
+
+    private final Function<JwsHeader, SecureDigestAlgorithm<?, ?>> signatureAlgorithmLocator;
+
+    private final Function<JweHeader, AeadAlgorithm> encryptionAlgorithmLocator;
+
+    private final Function<JweHeader, KeyAlgorithm<?, ?>> keyAlgorithmLocator;
+
+    private final Locator<? extends Key> keyLocator;
 
     private Decoder<String, byte[]> base64UrlDecoder = Decoders.BASE64URL;
 
@@ -82,29 +192,51 @@ public class DefaultJwtParser implements JwtParser {
 
     /**
      * TODO: remove this constructor before 1.0
+     *
      * @deprecated for backward compatibility only, see other constructors.
      */
+    @SuppressWarnings("DeprecatedIsStillUsed") // will remove before 1.0
     @Deprecated
-    public DefaultJwtParser() { }
+    public DefaultJwtParser() {
+        this.keyLocator = new ConstantKeyLocator(null, null);
+        this.signatureAlgorithmLocator = sigFn(Collections.<SecureDigestAlgorithm<?, ?>>emptyList());
+        this.keyAlgorithmLocator = keyFn(Collections.<KeyAlgorithm<?, ?>>emptyList());
+        this.encryptionAlgorithmLocator = encFn(Collections.<AeadAlgorithm>emptyList());
+        this.compressionCodecLocator = new DefaultCompressionCodecResolver();
+        this.enableUnsecuredJws = false;
+        this.enableUnsecuredDecompression = false;
+    }
 
-    DefaultJwtParser(SigningKeyResolver signingKeyResolver,
-                     Key key,
-                     byte[] keyBytes,
+    //SigningKeyResolver will be removed for 1.0:
+    @SuppressWarnings("deprecation")
+    DefaultJwtParser(Provider provider,
+                     SigningKeyResolver signingKeyResolver,
+                     boolean enableUnsecuredJws,
+                     boolean enableUnsecuredDecompression,
+                     Locator<? extends Key> keyLocator,
                      Clock clock,
                      long allowedClockSkewMillis,
                      Claims expectedClaims,
                      Decoder<String, byte[]> base64UrlDecoder,
                      Deserializer<Map<String, ?>> deserializer,
-                     CompressionCodecResolver compressionCodecResolver) {
+                     Locator<CompressionCodec> compressionCodecLocator,
+                     Collection<SecureDigestAlgorithm<?, ?>> extraSigAlgs,
+                     Collection<KeyAlgorithm<?, ?>> extraKeyAlgs,
+                     Collection<AeadAlgorithm> extraEncAlgs) {
+        this.provider = provider;
+        this.enableUnsecuredJws = enableUnsecuredJws;
+        this.enableUnsecuredDecompression = enableUnsecuredDecompression;
         this.signingKeyResolver = signingKeyResolver;
-        this.key = key;
-        this.keyBytes = keyBytes;
+        this.keyLocator = Assert.notNull(keyLocator, "Key Locator cannot be null.");
         this.clock = clock;
         this.allowedClockSkewMillis = allowedClockSkewMillis;
         this.expectedClaims = expectedClaims;
         this.base64UrlDecoder = base64UrlDecoder;
         this.deserializer = deserializer;
-        this.compressionCodecResolver = compressionCodecResolver;
+        this.signatureAlgorithmLocator = sigFn(extraSigAlgs);
+        this.keyAlgorithmLocator = keyFn(extraKeyAlgs);
+        this.encryptionAlgorithmLocator = encFn(extraEncAlgs);
+        this.compressionCodecLocator = Assert.notNull(compressionCodecLocator, "CompressionCodec locator cannot be null.");
     }
 
     @Override
@@ -188,24 +320,24 @@ public class DefaultJwtParser implements JwtParser {
     @Override
     public JwtParser setSigningKey(byte[] key) {
         Assert.notEmpty(key, "signing key cannot be null or empty.");
-        this.keyBytes = key;
-        return this;
+        return setSigningKey(Keys.hmacShaKeyFor(key));
     }
 
     @Override
     public JwtParser setSigningKey(String base64EncodedSecretKey) {
         Assert.hasText(base64EncodedSecretKey, "signing key cannot be null or empty.");
-        this.keyBytes = Decoders.BASE64.decode(base64EncodedSecretKey);
-        return this;
+        byte[] bytes = Decoders.BASE64.decode(base64EncodedSecretKey);
+        return setSigningKey(bytes);
     }
 
     @Override
-    public JwtParser setSigningKey(Key key) {
+    public JwtParser setSigningKey(final Key key) {
         Assert.notNull(key, "signing key cannot be null.");
-        this.key = key;
+        setSigningKeyResolver(new ConstantKeyLocator(key, null));
         return this;
     }
 
+    @SuppressWarnings("deprecation") // required until 1.0
     @Override
     public JwtParser setSigningKeyResolver(SigningKeyResolver signingKeyResolver) {
         Assert.notNull(signingKeyResolver, "SigningKeyResolver cannot be null.");
@@ -213,211 +345,351 @@ public class DefaultJwtParser implements JwtParser {
         return this;
     }
 
+    @SuppressWarnings("deprecation")
     @Override
     public JwtParser setCompressionCodecResolver(CompressionCodecResolver compressionCodecResolver) {
         Assert.notNull(compressionCodecResolver, "compressionCodecResolver cannot be null.");
-        this.compressionCodecResolver = compressionCodecResolver;
+        this.compressionCodecLocator = new CompressionCodecLocator(compressionCodecResolver);
         return this;
     }
 
     @Override
-    public boolean isSigned(String jwt) {
-
-        if (jwt == null) {
+    public boolean isSigned(String compact) {
+        if (compact == null) {
             return false;
         }
+        try {
+            final TokenizedJwt tokenized = jwtTokenizer.tokenize(compact);
+            return !(tokenized instanceof TokenizedJwe) && Strings.hasText(tokenized.getDigest());
+        } catch (MalformedJwtException e) {
+            return false;
+        }
+    }
 
-        int delimiterCount = 0;
+    private static boolean hasContentType(Header<?> header) {
+        return header != null && Strings.hasText(header.getContentType());
+    }
 
-        for (int i = 0; i < jwt.length(); i++) {
-            char c = jwt.charAt(i);
 
-            if (delimiterCount == 2) {
-                return !Character.isWhitespace(c) && c != SEPARATOR_CHAR;
-            }
+    /**
+     * Returns {@code true} IFF the specified payload starts with a <code>&#123;</code> character and ends with a
+     * <code>&#125;</code> character, ignoring any leading or trailing whitespace as defined by
+     * {@link Character#isWhitespace(char)}.  This does not guarantee JSON, just that it is likely JSON and
+     * should be passed to a JSON Deserializer to see if it is actually JSON.  If this {@code returns false}, it
+     * should be considered a byte[] payload and <em>not</em> delegated to a JSON Deserializer.
+     *
+     * @param payload the byte array that could be JSON
+     * @return {@code true} IFF the specified payload starts with a <code>&#123;</code> character and ends with a
+     * <code>&#125;</code> character, ignoring any leading or trailing whitespace as defined by
+     * {@link Character#isWhitespace(char)}
+     * @since JJWT_RELEASE_VERSION
+     */
+    private static boolean isLikelyJson(byte[] payload) {
 
-            if (c == SEPARATOR_CHAR) {
-                delimiterCount++;
-            }
+        int len = Bytes.length(payload);
+        if (len == 0) {
+            return false;
         }
 
-        return false;
-    }
+        int maxIndex = len - 1;
+        int jsonStartIndex = -1; // out of bounds means didn't find any
+        int jsonEndIndex = len; // out of bounds means didn't find any
 
-    @Override
-    public Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException {
+        for (int i = 0; i < len; i++) {
+            int c = payload[i];
+            if (c == '{') {
+                jsonStartIndex = i;
+                break;
+            }
+        }
+        if (jsonStartIndex == -1) { //exhausted entire payload, didn't find starting '{', can't be a JSON object
+            return false;
+        }
+        if (jsonStartIndex > 0) {
+            // we found content at the start of the payload, but before the first '{' character, so we need to check
+            // to see if any of it (when UTF-8 decoded) is not whitespace. If so, it can't be a valid JSON object.
+            byte[] leading = new byte[jsonStartIndex];
+            System.arraycopy(payload, 0, leading, 0, jsonStartIndex);
+            String s = new String(leading, StandardCharsets.UTF_8);
+            if (Strings.hasText(s)) { // found something before '{' that isn't whitespace; can't be a valid JSON object
+                return false;
+            }
+        }
 
-        // TODO, this logic is only need for a now deprecated code path
-        // remove this block in v1.0 (the equivalent is already in DefaultJwtParserBuilder)
-        if (this.deserializer == null) {
-            // try to find one based on the services available
-            // TODO: This util class will throw a UnavailableImplementationException here to retain behavior of previous version, remove in v1.0
-            this.deserializeJsonWith(LegacyServices.loadFirst(Deserializer.class));
+        for (int i = maxIndex; i > jsonStartIndex; i--) {
+            int c = payload[i];
+            if (c == '}') {
+                jsonEndIndex = i;
+                break;
+            }
         }
 
-        Assert.hasText(jwt, "JWT String argument cannot be null or empty.");
+        if (jsonEndIndex > maxIndex) { // found start '{' char, but no closing '} char. Can't be a JSON object
+            return false;
+        }
 
-        if ("..".equals(jwt)) {
-            String msg = "JWT string '..' is missing a header.";
-            throw new MalformedJwtException(msg);
+        if (jsonEndIndex < maxIndex) {
+            // We found content at the end of the payload, after the last '}' character.  We need to check to see if
+            // any of it (when UTF-8 decoded) is not whitespace. If so, it's not a valid JSON object payload.
+            int size = maxIndex - jsonEndIndex;
+            byte[] trailing = new byte[size];
+            System.arraycopy(payload, jsonEndIndex + 1, trailing, 0, size);
+            String s = new String(trailing, StandardCharsets.UTF_8);
+            return !Strings.hasText(s); // if just whitespace after last '}', we can assume JSON and try and parse it
         }
 
-        String base64UrlEncodedHeader = null;
-        String base64UrlEncodedPayload = null;
-        String base64UrlEncodedDigest = null;
+        return true;
+    }
 
-        int delimiterCount = 0;
+    private void verifySignature(final TokenizedJwt tokenized, final JwsHeader jwsHeader, final String alg,
+                                 @SuppressWarnings("deprecation") SigningKeyResolver resolver,
+                                 Claims claims, byte[] payload) {
 
-        StringBuilder sb = new StringBuilder(128);
+        Assert.notNull(resolver, "SigningKeyResolver instance cannot be null.");
 
-        for (char c : jwt.toCharArray()) {
+        SecureDigestAlgorithm<?, Key> algorithm;
+        try {
+            algorithm = (SecureDigestAlgorithm<?, Key>) signatureAlgorithmLocator.apply(jwsHeader);
+        } catch (UnsupportedJwtException e) {
+            //For backwards compatibility.  TODO: remove this try/catch block for 1.0 and let UnsupportedJwtException propagate
+            String msg = "Unsupported signature algorithm '" + alg + "'";
+            throw new SignatureException(msg, e);
+        }
+        Assert.stateNotNull(algorithm, "JWS Signature Algorithm cannot be null.");
 
-            if (c == SEPARATOR_CHAR) {
+        //digitally signed, let's assert the signature:
+        Key key;
+        if (claims != null) {
+            key = resolver.resolveSigningKey(jwsHeader, claims);
+        } else {
+            key = resolver.resolveSigningKey(jwsHeader, payload);
+        }
+        if (key == null) {
+            String msg = "Cannot verify JWS signature: unable to locate signature verification key for JWS with header: " + jwsHeader;
+            throw new UnsupportedJwtException(msg);
+        }
 
-                CharSequence tokenSeq = Strings.clean(sb);
-                String token = tokenSeq != null ? tokenSeq.toString() : null;
+        //re-create the jwt part without the signature.  This is what is needed for signature verification:
+        String jwtWithoutSignature = tokenized.getProtected() + SEPARATOR_CHAR + tokenized.getBody();
 
-                if (delimiterCount == 0) {
-                    base64UrlEncodedHeader = token;
-                } else if (delimiterCount == 1) {
-                    base64UrlEncodedPayload = token;
-                }
+        byte[] data = jwtWithoutSignature.getBytes(StandardCharsets.US_ASCII);
+        byte[] signature = base64UrlDecode(tokenized.getDigest(), "JWS signature");
 
-                delimiterCount++;
-                sb.setLength(0);
-            } else {
-                sb.append(c);
+        try {
+            VerifySecureDigestRequest<Key> request =
+                    new DefaultVerifySecureDigestRequest<>(data, this.provider, null, key, signature);
+            if (!algorithm.verify(request)) {
+                String msg = "JWT signature does not match locally computed signature. JWT validity cannot be " +
+                        "asserted and should not be trusted.";
+                throw new SignatureException(msg);
             }
+        } catch (WeakKeyException e) {
+            throw e;
+        } catch (InvalidKeyException | IllegalArgumentException e) {
+            String algId = algorithm.getId();
+            String msg = "The parsed JWT indicates it was signed with the '" + algId + "' signature " +
+                    "algorithm, but the provided " + key.getClass().getName() + " key may " +
+                    "not be used to verify " + algId + " signatures.  Because the specified " +
+                    "key reflects a specific and expected algorithm, and the JWT does not reflect " +
+                    "this algorithm, it is likely that the JWT was not expected and therefore should not be " +
+                    "trusted.  Another possibility is that the parser was provided the incorrect " +
+                    "signature verification key, but this cannot be assumed for security reasons.";
+            throw new UnsupportedJwtException(msg, e);
         }
+    }
 
-        if (delimiterCount != 2) {
-            String msg = "JWT strings must contain exactly 2 period characters. Found: " + delimiterCount;
-            throw new MalformedJwtException(msg);
-        }
-        if (sb.length() > 0) {
-            base64UrlEncodedDigest = sb.toString();
-        }
-
+    @Override
+    public Jwt<?, ?> parse(String compact) throws ExpiredJwtException, MalformedJwtException, SignatureException {
 
-        // =============== Header =================
-        Header header = null;
+        // TODO, this logic is only need for a now deprecated code path
+        // remove this block in v1.0 (the equivalent is already in DefaultJwtParserBuilder)
+        if (this.deserializer == null) {
+            // try to find one based on the services available
+            // TODO: This util class will throw a UnavailableImplementationException here to retain behavior of previous version, remove in v1.0
+            //noinspection deprecation
+            this.deserializer = LegacyServices.loadFirst(Deserializer.class);
+        }
 
-        CompressionCodec compressionCodec = null;
+        Assert.hasText(compact, "JWT String cannot be null or empty.");
 
-        if (base64UrlEncodedHeader != null) {
-            byte[] bytes = base64UrlDecoder.decode(base64UrlEncodedHeader);
-            String origValue = new String(bytes, Strings.UTF_8);
-            Map<String, Object> m = (Map<String, Object>) readValue(origValue);
+        final TokenizedJwt tokenized = jwtTokenizer.tokenize(compact);
+        final String base64UrlHeader = tokenized.getProtected();
+        if (!Strings.hasText(base64UrlHeader)) {
+            String msg = "Compact JWT strings MUST always have a Base64Url protected header per https://tools.ietf.org/html/rfc7519#section-7.2 (steps 2-4).";
+            throw new MalformedJwtException(msg);
+        }
 
-            if (base64UrlEncodedDigest != null) {
-                header = new DefaultJwsHeader(m);
-            } else {
-                header = new DefaultHeader(m);
-            }
+        // =============== Header =================
+        final byte[] headerBytes = base64UrlDecode(base64UrlHeader, "protected header");
+        Map<String, ?> m = readValue(headerBytes, "protected header");
+        Header<?> header;
+        try {
+            header = tokenized.createHeader(m);
+        } catch (Exception e) {
+            String msg = "Invalid protected header: " + e.getMessage();
+            throw new MalformedJwtException(msg, e);
+        }
 
-            compressionCodec = compressionCodecResolver.resolveCompressionCodec(header);
+        // https://tools.ietf.org/html/rfc7515#section-10.7 , second-to-last bullet point, note the use of 'always':
+        //
+        //   *  Require that the "alg" Header Parameter be carried in the JWS
+        //      Protected Header.  (This is always the case when using the JWS
+        //      Compact Serialization and is the approach taken by CMS [RFC6211].)
+        //
+        final String alg = Strings.clean(header.getAlgorithm());
+        if (!Strings.hasText(alg)) {
+            String msg = tokenized instanceof TokenizedJwe ? MISSING_JWE_ALG_MSG : MISSING_JWS_ALG_MSG;
+            throw new MalformedJwtException(msg);
         }
+        final boolean unsecured = Jwts.SIG.NONE.getId().equalsIgnoreCase(alg);
 
-        // =============== Body =================
-        String payload = ""; // https://github.com/jwtk/jjwt/pull/540
-        if (base64UrlEncodedPayload != null) {
-            byte[] bytes = base64UrlDecoder.decode(base64UrlEncodedPayload);
-            if (compressionCodec != null) {
-                bytes = compressionCodec.decompress(bytes);
+        final String base64UrlDigest = tokenized.getDigest();
+        final boolean hasDigest = Strings.hasText(base64UrlDigest);
+        if (unsecured) {
+            if (tokenized instanceof TokenizedJwe) {
+                throw new MalformedJwtException(JWE_NONE_MSG);
+            }
+            // Unsecured JWTs are disabled by default per the RFC:
+            if (!enableUnsecuredJws) {
+                String msg = UNSECURED_DISABLED_MSG_PREFIX + header;
+                throw new UnsupportedJwtException(msg);
             }
-            payload = new String(bytes, Strings.UTF_8);
+            if (hasDigest) {
+                throw new MalformedJwtException(JWS_NONE_SIG_MISMATCH_MSG);
+            }
+        } else if (!hasDigest) { // something other than 'none'.  Must have a digest component:
+            String fmt = tokenized instanceof TokenizedJwe ? MISSING_JWE_DIGEST_MSG_FMT : MISSING_JWS_DIGEST_MSG_FMT;
+            String msg = String.format(fmt, alg);
+            throw new MalformedJwtException(msg);
         }
 
-        Claims claims = null;
-
-        if (!payload.isEmpty() && payload.charAt(0) == '{' && payload.charAt(payload.length() - 1) == '}') { //likely to be json, parse it:
-            Map<String, Object> claimsMap = (Map<String, Object>) readValue(payload);
-            claims = new DefaultClaims(claimsMap);
+        // =============== Body =================
+        byte[] payload = base64UrlDecode(tokenized.getBody(), "payload");
+        if (tokenized instanceof TokenizedJwe && Arrays.length(payload) == 0) { // Only JWS body can be empty per https://github.com/jwtk/jjwt/pull/540
+            String msg = "Compact JWE strings MUST always contain a payload (ciphertext).";
+            throw new MalformedJwtException(msg);
         }
 
-        // =============== Signature =================
-        if (base64UrlEncodedDigest != null) { //it is signed - validate the signature
-
-            JwsHeader jwsHeader = (JwsHeader) header;
+        byte[] iv = null;
+        byte[] tag = null;
+        if (tokenized instanceof TokenizedJwe) {
 
-            SignatureAlgorithm algorithm = null;
+            TokenizedJwe tokenizedJwe = (TokenizedJwe) tokenized;
+            JweHeader jweHeader = (JweHeader) header;
 
-            if (header != null) {
-                String alg = jwsHeader.getAlgorithm();
-                if (Strings.hasText(alg)) {
-                    algorithm = SignatureAlgorithm.forName(alg);
+            byte[] cekBytes = Bytes.EMPTY; //ignored unless using an encrypted key algorithm
+            String base64Url = tokenizedJwe.getEncryptedKey();
+            if (Strings.hasText(base64Url)) {
+                cekBytes = base64UrlDecode(base64Url, "JWE encrypted key");
+                if (Arrays.length(cekBytes) == 0) {
+                    String msg = "Compact JWE string represents an encrypted key, but the key is empty.";
+                    throw new MalformedJwtException(msg);
                 }
             }
 
-            if (algorithm == null || algorithm == SignatureAlgorithm.NONE) {
-                //it is plaintext, but it has a signature.  This is invalid:
-                String msg = "JWT string has a digest/signature, but the header does not reference a valid signature " +
-                    "algorithm.";
+            base64Url = tokenizedJwe.getIv();
+            if (Strings.hasText(base64Url)) {
+                iv = base64UrlDecode(base64Url, "JWE Initialization Vector");
+            }
+            if (Arrays.length(iv) == 0) {
+                String msg = "Compact JWE strings must always contain an Initialization Vector.";
                 throw new MalformedJwtException(msg);
             }
 
-            if (key != null && keyBytes != null) {
-                throw new IllegalStateException("A key object and key bytes cannot both be specified. Choose either.");
-            } else if ((key != null || keyBytes != null) && signingKeyResolver != null) {
-                String object = key != null ? "a key object" : "key bytes";
-                throw new IllegalStateException("A signing key resolver and " + object + " cannot both be specified. Choose either.");
+            // The AAD (Additional Authenticated Data) scheme for compact JWEs is to use the ASCII bytes of the
+            // raw base64url text as the AAD, and NOT the base64url-decoded bytes per
+            // https://www.rfc-editor.org/rfc/rfc7516.html#section-5.1, Step 14.
+            final byte[] aad = base64UrlHeader.getBytes(StandardCharsets.US_ASCII);
+
+            base64Url = base64UrlDigest;
+            //guaranteed to be non-empty via the `alg` + digest check above:
+            Assert.hasText(base64Url, "JWE AAD Authentication Tag cannot be null or empty.");
+            tag = base64UrlDecode(base64Url, "JWE AAD Authentication Tag");
+            if (Arrays.length(tag) == 0) {
+                String msg = "Compact JWE strings must always contain an AAD Authentication Tag.";
+                throw new MalformedJwtException(msg);
             }
 
-            //digitally signed, let's assert the signature:
-            Key key = this.key;
-
-            if (key == null) { //fall back to keyBytes
-
-                byte[] keyBytes = this.keyBytes;
-
-                if (Objects.isEmpty(keyBytes) && signingKeyResolver != null) { //use the signingKeyResolver
-                    if (claims != null) {
-                        key = signingKeyResolver.resolveSigningKey(jwsHeader, claims);
-                    } else {
-                        key = signingKeyResolver.resolveSigningKey(jwsHeader, payload);
-                    }
-                }
+            String enc = jweHeader.getEncryptionAlgorithm();
+            if (!Strings.hasText(enc)) {
+                throw new MalformedJwtException(MISSING_ENC_MSG);
+            }
+            final AeadAlgorithm encAlg = this.encryptionAlgorithmLocator.apply(jweHeader);
+            Assert.stateNotNull(encAlg, "JWE Encryption Algorithm cannot be null.");
 
-                if (!Objects.isEmpty(keyBytes)) {
+            @SuppressWarnings("rawtypes") final KeyAlgorithm keyAlg = this.keyAlgorithmLocator.apply(jweHeader);
+            Assert.stateNotNull(keyAlg, "JWE Key Algorithm cannot be null.");
 
-                    Assert.isTrue(algorithm.isHmac(),
-                        "Key bytes can only be specified for HMAC signatures. Please specify a PublicKey or PrivateKey instance.");
+            final Key key = this.keyLocator.locate(jweHeader);
+            if (key == null) {
+                String msg = "Cannot decrypt JWE payload: unable to locate key for JWE with header: " + jweHeader;
+                throw new UnsupportedJwtException(msg);
+            }
 
-                    key = new SecretKeySpec(keyBytes, algorithm.getJcaName());
-                }
+            DecryptionKeyRequest<Key> request =
+                    new DefaultDecryptionKeyRequest<>(cekBytes, this.provider, null, jweHeader, encAlg, key);
+            final SecretKey cek = keyAlg.getDecryptionKey(request);
+            if (cek == null) {
+                String msg = "The '" + keyAlg.getId() + "' JWE key algorithm did not return a decryption key. " +
+                        "Unable to perform '" + encAlg.getId() + "' decryption.";
+                throw new IllegalStateException(msg);
             }
 
-            Assert.notNull(key, "A signing key must be specified if the specified JWT is digitally signed.");
+            DecryptAeadRequest decryptRequest =
+                    new DefaultAeadResult(this.provider, null, payload, cek, aad, tag, iv);
+            Message<byte[]> result = encAlg.decrypt(decryptRequest);
+            payload = result.getPayload();
 
-            //re-create the jwt part without the signature.  This is what needs to be signed for verification:
-            String jwtWithoutSignature = base64UrlEncodedHeader + SEPARATOR_CHAR;
-            if (base64UrlEncodedPayload != null) {
-              jwtWithoutSignature += base64UrlEncodedPayload;
+        } else if (hasDigest && this.signingKeyResolver == null) { //TODO: for 1.0, remove the == null check
+            // not using a signing key resolver, so we can verify the signature before reading the body, which is
+            // always safer:
+            verifySignature(tokenized, ((JwsHeader) header), alg, new LocatingKeyResolver(this.keyLocator), null, null);
+        }
+
+        CompressionCodec compressionCodec = compressionCodecLocator.locate(header);
+        if (compressionCodec != null) {
+            if (unsecured && !enableUnsecuredDecompression) {
+                String msg = String.format(UNPROTECTED_DECOMPRESSION_MSG, compressionCodec.getId());
+                throw new UnsupportedJwtException(msg);
             }
+            payload = compressionCodec.decompress(payload);
+        }
 
-            JwtSignatureValidator validator;
+        Claims claims = null;
+        if (!hasContentType(header) // If there is a content type set, then the application using JJWT is expected
+                //                     to convert the byte payload themselves based on this content type
+                //                     https://www.rfc-editor.org/rfc/rfc7515.html#section-4.1.10 :
+                //
+                //                     "This parameter is ignored by JWS implementations; any processing of this
+                //                      parameter is performed by the JWS application."
+                //
+                && isLikelyJson(payload)) { // likely to be json, parse it:
+            Map<String, ?> claimsMap = readValue(payload, "claims");
             try {
-                algorithm.assertValidVerificationKey(key); //since 0.10.0: https://github.com/jwtk/jjwt/issues/334
-                validator = createSignatureValidator(algorithm, key);
-            } catch (WeakKeyException e) {
-                throw e;
-            } catch (InvalidKeyException | IllegalArgumentException e) {
-                String algName = algorithm.getValue();
-                String msg = "The parsed JWT indicates it was signed with the '" + algName + "' signature " +
-                    "algorithm, but the provided " + key.getClass().getName() + " key may " +
-                    "not be used to verify " + algName + " signatures.  Because the specified " +
-                    "key reflects a specific and expected algorithm, and the JWT does not reflect " +
-                    "this algorithm, it is likely that the JWT was not expected and therefore should not be " +
-                    "trusted.  Another possibility is that the parser was provided the incorrect " +
-                    "signature verification key, but this cannot be assumed for security reasons.";
-                throw new UnsupportedJwtException(msg, e);
+                claims = new DefaultClaims(claimsMap);
+            } catch (Exception e) {
+                String msg = "Invalid claims: " + e.getMessage();
+                throw new MalformedJwtException(msg, e);
             }
+        }
 
-            if (!validator.isValid(jwtWithoutSignature, base64UrlEncodedDigest)) {
-                String msg = "JWT signature does not match locally computed signature. JWT validity cannot be " +
-                    "asserted and should not be trusted.";
-                throw new SignatureException(msg);
-            }
+        Jwt<?, ?> jwt;
+        Object body = claims != null ? claims : payload;
+        if (header instanceof JweHeader) {
+            jwt = new DefaultJwe<>((JweHeader) header, body, iv, tag);
+        } else if (hasDigest) {
+            JwsHeader jwsHeader = Assert.isInstanceOf(JwsHeader.class, header, "JwsHeader required.");
+            jwt = new DefaultJws<>(jwsHeader, body, base64UrlDigest);
+        } else {
+            //noinspection rawtypes
+            jwt = new DefaultJwt(header, body);
+        }
+
+        // =============== Signature =================
+        if (hasDigest && signingKeyResolver != null) { // TODO: remove for 1.0
+            // A SigningKeyResolver has been configured, and due to it's API, we have to verify the signature after
+            // parsing the body.  This can be a security risk, so it needs to be removed before 1.0
+            verifySignature(tokenized, ((JwsHeader) header), alg, this.signingKeyResolver, claims, payload);
         }
 
         final boolean allowSkew = this.allowedClockSkewMillis > 0;
@@ -439,11 +711,11 @@ public class DefaultJwtParser implements JwtParser {
                     String expVal = DateFormats.formatIso8601(exp, false);
                     String nowVal = DateFormats.formatIso8601(now, false);
 
-                    long differenceMillis = maxTime - exp.getTime();
+                    long differenceMillis = nowTime - exp.getTime();
 
                     String msg = "JWT expired at " + expVal + ". Current time: " + nowVal + ", a difference of " +
-                        differenceMillis + " milliseconds.  Allowed clock skew: " +
-                        this.allowedClockSkewMillis + " milliseconds.";
+                            differenceMillis + " milliseconds.  Allowed clock skew: " +
+                            this.allowedClockSkewMillis + " milliseconds.";
                     throw new ExpiredJwtException(header, claims, msg);
                 }
             }
@@ -462,9 +734,9 @@ public class DefaultJwtParser implements JwtParser {
                     long differenceMillis = nbf.getTime() - minTime;
 
                     String msg = "JWT must not be accepted before " + nbfVal + ". Current time: " + nowVal +
-                        ", a difference of " +
-                        differenceMillis + " milliseconds.  Allowed clock skew: " +
-                        this.allowedClockSkewMillis + " milliseconds.";
+                            ", a difference of " +
+                            differenceMillis + " milliseconds.  Allowed clock skew: " +
+                            this.allowedClockSkewMillis + " milliseconds.";
                     throw new PrematureJwtException(header, claims, msg);
                 }
             }
@@ -472,13 +744,7 @@ public class DefaultJwtParser implements JwtParser {
             validateExpectedClaims(header, claims);
         }
 
-        Object body = claims != null ? claims : payload;
-
-        if (base64UrlEncodedDigest != null) {
-            return new DefaultJws<>((JwsHeader) header, body, base64UrlEncodedDigest);
-        } else {
-            return new DefaultJwt<>(header, body);
-        }
+        return jwt;
     }
 
     /**
@@ -491,7 +757,7 @@ public class DefaultJwtParser implements JwtParser {
         return o;
     }
 
-    private void validateExpectedClaims(Header header, Claims claims) {
+    private void validateExpectedClaims(Header<?> header, Claims claims) {
 
         for (String expectedClaimName : expectedClaims.keySet()) {
 
@@ -503,110 +769,90 @@ public class DefaultJwtParser implements JwtParser {
                     actualClaimValue = claims.get(expectedClaimName, Date.class);
                 } catch (Exception e) {
                     String msg = "JWT Claim '" + expectedClaimName + "' was expected to be a Date, but its value " +
-                        "cannot be converted to a Date using current heuristics.  Value: " + actualClaimValue;
-                    throw new IncorrectClaimException(header, claims, msg);
+                            "cannot be converted to a Date using current heuristics.  Value: " + actualClaimValue;
+                    throw new IncorrectClaimException(header, claims, expectedClaimName, expectedClaimValue, msg);
                 }
             }
 
-            InvalidClaimException invalidClaimException = null;
-
             if (actualClaimValue == null) {
-
-                String msg = String.format(ClaimJwtException.MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
-                    expectedClaimName, expectedClaimValue);
-
-                invalidClaimException = new MissingClaimException(header, claims, msg);
-
+                String msg = String.format(MISSING_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
+                        expectedClaimName, expectedClaimValue);
+                throw new MissingClaimException(header, claims, expectedClaimName, expectedClaimValue, msg);
             } else if (!expectedClaimValue.equals(actualClaimValue)) {
-
-                String msg = String.format(ClaimJwtException.INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
-                    expectedClaimName, expectedClaimValue, actualClaimValue);
-
-                invalidClaimException = new IncorrectClaimException(header, claims, msg);
-            }
-
-            if (invalidClaimException != null) {
-                invalidClaimException.setClaimName(expectedClaimName);
-                invalidClaimException.setClaimValue(expectedClaimValue);
-                throw invalidClaimException;
+                String msg = String.format(INCORRECT_EXPECTED_CLAIM_MESSAGE_TEMPLATE,
+                        expectedClaimName, expectedClaimValue, actualClaimValue);
+                throw new IncorrectClaimException(header, claims, expectedClaimName, expectedClaimValue, msg);
             }
         }
     }
 
-    /*
-     * @since 0.5 mostly to allow testing overrides
-     */
-    protected JwtSignatureValidator createSignatureValidator(SignatureAlgorithm alg, Key key) {
-        return new DefaultJwtSignatureValidator(alg, key, base64UrlDecoder);
-    }
-
     @Override
     public <T> T parse(String compact, JwtHandler<T> handler)
-        throws ExpiredJwtException, MalformedJwtException, SignatureException {
+            throws ExpiredJwtException, MalformedJwtException, SignatureException {
         Assert.notNull(handler, "JwtHandler argument cannot be null.");
         Assert.hasText(compact, "JWT String argument cannot be null or empty.");
 
-        Jwt jwt = parse(compact);
+        Jwt<?, ?> jwt = parse(compact);
 
         if (jwt instanceof Jws) {
-            Jws jws = (Jws) jwt;
-            Object body = jws.getBody();
+            Jws<?> jws = (Jws<?>) jwt;
+            Object body = jws.getPayload();
             if (body instanceof Claims) {
                 return handler.onClaimsJws((Jws<Claims>) jws);
             } else {
-                return handler.onPlaintextJws((Jws<String>) jws);
+                return handler.onContentJws((Jws<byte[]>) jws);
+            }
+        } else if (jwt instanceof Jwe) {
+            Jwe<?> jwe = (Jwe<?>) jwt;
+            Object body = jwe.getPayload();
+            if (body instanceof Claims) {
+                return handler.onClaimsJwe((Jwe<Claims>) jwe);
+            } else {
+                return handler.onContentJwe((Jwe<byte[]>) jwe);
             }
         } else {
-            Object body = jwt.getBody();
+            Object body = jwt.getPayload();
             if (body instanceof Claims) {
-                return handler.onClaimsJwt((Jwt<Header, Claims>) jwt);
+                return handler.onClaimsJwt((Jwt<UnprotectedHeader, Claims>) jwt);
             } else {
-                return handler.onPlaintextJwt((Jwt<Header, String>) jwt);
+                return handler.onContentJwt((Jwt<UnprotectedHeader, byte[]>) jwt);
             }
         }
     }
 
     @Override
-    public Jwt<Header, String> parsePlaintextJwt(String plaintextJwt) {
-        return parse(plaintextJwt, new JwtHandlerAdapter<Jwt<Header, String>>() {
+    public Jwt<UnprotectedHeader, byte[]> parseContentJwt(String compact) {
+        return parse(compact, new JwtHandlerAdapter<Jwt<UnprotectedHeader, byte[]>>() {
             @Override
-            public Jwt<Header, String> onPlaintextJwt(Jwt<Header, String> jwt) {
+            public Jwt<UnprotectedHeader, byte[]> onContentJwt(Jwt<UnprotectedHeader, byte[]> jwt) {
                 return jwt;
             }
         });
     }
 
     @Override
-    public Jwt<Header, Claims> parseClaimsJwt(String claimsJwt) {
-        try {
-            return parse(claimsJwt, new JwtHandlerAdapter<Jwt<Header, Claims>>() {
-                @Override
-                public Jwt<Header, Claims> onClaimsJwt(Jwt<Header, Claims> jwt) {
-                    return jwt;
-                }
-            });
-        } catch (IllegalArgumentException iae) {
-            throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
-        }
+    public Jwt<UnprotectedHeader, Claims> parseClaimsJwt(String compact) {
+        return parse(compact, new JwtHandlerAdapter<Jwt<UnprotectedHeader, Claims>>() {
+            @Override
+            public Jwt<UnprotectedHeader, Claims> onClaimsJwt(Jwt<UnprotectedHeader, Claims> jwt) {
+                return jwt;
+            }
+        });
     }
 
     @Override
-    public Jws<String> parsePlaintextJws(String plaintextJws) {
-        try {
-            return parse(plaintextJws, new JwtHandlerAdapter<Jws<String>>() {
-                @Override
-                public Jws<String> onPlaintextJws(Jws<String> jws) {
-                    return jws;
-                }
-            });
-        } catch (IllegalArgumentException iae) {
-            throw new UnsupportedJwtException("Signed JWSs are not supported.", iae);
-        }
+    public Jws<byte[]> parseContentJws(String compact) {
+        return parse(compact, new JwtHandlerAdapter<Jws<byte[]>>() {
+            @Override
+            public Jws<byte[]> onContentJws(Jws<byte[]> jws) {
+                return jws;
+            }
+        });
     }
 
     @Override
-    public Jws<Claims> parseClaimsJws(String claimsJws) {
-        return parse(claimsJws, new JwtHandlerAdapter<Jws<Claims>>() {
+    public Jws<Claims> parseClaimsJws(String compact) {
+        return parse(compact, new JwtHandlerAdapter<Jws<Claims>>() {
             @Override
             public Jws<Claims> onClaimsJws(Jws<Claims> jws) {
                 return jws;
@@ -614,9 +860,41 @@ public class DefaultJwtParser implements JwtParser {
         });
     }
 
-    @SuppressWarnings("unchecked")
-    protected Map<String, ?> readValue(String val) {
-        byte[] bytes = val.getBytes(Strings.UTF_8);
-        return deserializer.deserialize(bytes);
+    @Override
+    public Jwe<byte[]> parseContentJwe(String compact) throws JwtException {
+        return parse(compact, new JwtHandlerAdapter<Jwe<byte[]>>() {
+            @Override
+            public Jwe<byte[]> onContentJwe(Jwe<byte[]> jwe) {
+                return jwe;
+            }
+        });
+    }
+
+    @Override
+    public Jwe<Claims> parseClaimsJwe(String compact) throws JwtException {
+        return parse(compact, new JwtHandlerAdapter<Jwe<Claims>>() {
+            @Override
+            public Jwe<Claims> onClaimsJwe(Jwe<Claims> jwe) {
+                return jwe;
+            }
+        });
+    }
+
+    protected byte[] base64UrlDecode(String base64UrlEncoded, String name) {
+        try {
+            return base64UrlDecoder.decode(base64UrlEncoded);
+        } catch (DecodingException e) {
+            String msg = "Invalid Base64Url " + name + ": " + base64UrlEncoded;
+            throw new MalformedJwtException(msg, e);
+        }
+    }
+
+    protected Map<String, ?> readValue(byte[] bytes, final String name) {
+        try {
+            return deserializer.deserialize(bytes);
+        } catch (MalformedJwtException | DeserializationException e) {
+            String s = new String(bytes, StandardCharsets.UTF_8);
+            throw new MalformedJwtException("Unable to read " + name + " JSON: " + s, e);
+        }
     }
 }
