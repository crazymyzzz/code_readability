@@ -20,13 +20,17 @@ import io.jsonwebtoken.CompressionCodecResolver;
 import io.jsonwebtoken.CompressionCodecs;
 import io.jsonwebtoken.CompressionException;
 import io.jsonwebtoken.Header;
+import io.jsonwebtoken.Locator;
+import io.jsonwebtoken.impl.lang.IdRegistry;
 import io.jsonwebtoken.impl.lang.Services;
 import io.jsonwebtoken.lang.Assert;
+import io.jsonwebtoken.lang.Registry;
 import io.jsonwebtoken.lang.Strings;
 
+import java.util.Collection;
 import java.util.Collections;
-import java.util.HashMap;
-import java.util.Map;
+import java.util.LinkedHashSet;
+import java.util.Set;
 
 /**
  * Default implementation of {@link CompressionCodecResolver} that supports the following:
@@ -36,63 +40,61 @@ import java.util.Map;
  * nothing and returns {@code null} to the caller, indicating no compression was used.</li>
  * <li>If the header has a {@code zip} value of {@code DEF}, a {@link DeflateCompressionCodec} will be returned.</li>
  * <li>If the header has a {@code zip} value of {@code GZIP}, a {@link GzipCompressionCodec} will be returned.</li>
- * <li>If the header has any other {@code zip} value, a {@link CompressionException} is thrown to reflect an
- * unrecognized algorithm.</li>
+ * <li>If the header has any other {@code zip} value, it tries to find corresponding {@code CompressionCodec} that
+ * matches that {@link CompressionCodec#getId() id}. If it finds a match, it returns it.</li>
+ * <li>If a matching {@code CompressionCodec} is not found for the specified {@code zip} value,
+ * a {@link CompressionException} is thrown to reflect an unrecognized algorithm.</li>
  * </ul>
  *
  * <p>If you want to use a compression algorithm other than {@code DEF} or {@code GZIP}, you must implement your own
  * {@link CompressionCodecResolver} and specify that when
  * {@link io.jsonwebtoken.JwtBuilder#compressWith(CompressionCodec) building} and
- * {@link io.jsonwebtoken.JwtParser#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
+ * {@link io.jsonwebtoken.JwtParserBuilder#setCompressionCodecResolver(CompressionCodecResolver) parsing} JWTs.</p>
  *
  * @see DeflateCompressionCodec
  * @see GzipCompressionCodec
  * @since 0.6.0
  */
-public class DefaultCompressionCodecResolver implements CompressionCodecResolver {
+public class DefaultCompressionCodecResolver implements CompressionCodecResolver, Locator<CompressionCodec> {
 
-    private static final String MISSING_COMPRESSION_MESSAGE = "Unable to find an implementation for compression algorithm [%s] using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-impl.jar, or your own .jar for custom implementations.";
+    private static final String MISSING_COMPRESSION_MESSAGE = "Unable to find an implementation for compression " +
+            "algorithm [%s] using java.util.ServiceLoader or via any specified extra CompressionCodec instances. " +
+            "Ensure you include a backing implementation .jar in the classpath, for example jjwt-impl.jar, or " +
+            "your own .jar for custom implementations, or use the JwtParser.addCompressionCodecs configuration " +
+            "method.";
 
-    private final Map<String, CompressionCodec> codecs;
+    private final Registry<String, CompressionCodec> codecs;
 
     public DefaultCompressionCodecResolver() {
-        Map<String, CompressionCodec> codecMap = new HashMap<>();
-        for (CompressionCodec codec : Services.loadAll(CompressionCodec.class)) {
-            codecMap.put(codec.getAlgorithmName().toUpperCase(), codec);
-        }
-
-        codecMap.put(CompressionCodecs.DEFLATE.getAlgorithmName().toUpperCase(), CompressionCodecs.DEFLATE);
-        codecMap.put(CompressionCodecs.GZIP.getAlgorithmName().toUpperCase(), CompressionCodecs.GZIP);
+        this(Collections.<CompressionCodec>emptySet());
+    }
 
-        codecs = Collections.unmodifiableMap(codecMap);
+    public DefaultCompressionCodecResolver(Collection<CompressionCodec> extraCodecs) {
+        Assert.notNull(extraCodecs, "extraCodecs cannot be null.");
+        Set<CompressionCodec> codecs = new LinkedHashSet<>(Services.loadAll(CompressionCodec.class));
+        codecs.addAll(extraCodecs);
+        codecs.add(CompressionCodecs.DEFLATE); // standard ones are added last so they can't be accidentally replaced
+        codecs.add(CompressionCodecs.GZIP);
+        this.codecs = new IdRegistry<>("CompressionCodec", codecs);
     }
 
     @Override
-    public CompressionCodec resolveCompressionCodec(Header header) {
-        String cmpAlg = getAlgorithmFromHeader(header);
-
-        final boolean hasCompressionAlgorithm = Strings.hasText(cmpAlg);
-
-        if (!hasCompressionAlgorithm) {
+    public CompressionCodec locate(Header<?> header) {
+        Assert.notNull(header, "Header cannot be null.");
+        String id = header.getCompressionAlgorithm();
+        if (!Strings.hasText(id)) {
             return null;
         }
-        return byName(cmpAlg);
-    }
-
-    private String getAlgorithmFromHeader(Header header) {
-        Assert.notNull(header, "header cannot be null.");
-
-        return header.getCompressionAlgorithm();
-    }
-
-    private CompressionCodec byName(String name) {
-        Assert.hasText(name, "'name' must not be empty");
-
-        CompressionCodec codec = codecs.get(name.toUpperCase());
+        CompressionCodec codec = codecs.find(id);
         if (codec == null) {
-            throw new CompressionException(String.format(MISSING_COMPRESSION_MESSAGE, name));
+            String msg = String.format(MISSING_COMPRESSION_MESSAGE, id);
+            throw new CompressionException(msg);
         }
-
         return codec;
     }
+
+    @Override
+    public CompressionCodec resolveCompressionCodec(Header<?> header) {
+        return locate(header);
+    }
 }
