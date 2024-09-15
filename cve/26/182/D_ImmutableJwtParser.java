@@ -19,13 +19,15 @@ import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.Clock;
 import io.jsonwebtoken.CompressionCodecResolver;
 import io.jsonwebtoken.ExpiredJwtException;
-import io.jsonwebtoken.Header;
+import io.jsonwebtoken.Jwe;
 import io.jsonwebtoken.Jws;
 import io.jsonwebtoken.Jwt;
+import io.jsonwebtoken.JwtException;
 import io.jsonwebtoken.JwtHandler;
 import io.jsonwebtoken.JwtParser;
 import io.jsonwebtoken.MalformedJwtException;
 import io.jsonwebtoken.SigningKeyResolver;
+import io.jsonwebtoken.UnprotectedHeader;
 import io.jsonwebtoken.UnsupportedJwtException;
 import io.jsonwebtoken.io.Decoder;
 import io.jsonwebtoken.io.Deserializer;
@@ -139,12 +141,12 @@ class ImmutableJwtParser implements JwtParser {
     }
 
     @Override
-    public boolean isSigned(String jwt) {
-        return this.jwtParser.isSigned(jwt);
+    public boolean isSigned(String compact) {
+        return this.jwtParser.isSigned(compact);
     }
 
     @Override
-    public Jwt parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
+    public Jwt<?,?> parse(String jwt) throws ExpiredJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
         return this.jwtParser.parse(jwt);
     }
 
@@ -154,22 +156,32 @@ class ImmutableJwtParser implements JwtParser {
     }
 
     @Override
-    public Jwt<Header, String> parsePlaintextJwt(String plaintextJwt) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
-        return this.jwtParser.parsePlaintextJwt(plaintextJwt);
+    public Jwt<UnprotectedHeader, byte[]> parseContentJwt(String jwt) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
+        return this.jwtParser.parseContentJwt(jwt);
     }
 
     @Override
-    public Jwt<Header, Claims> parseClaimsJwt(String claimsJwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
-        return this.jwtParser.parseClaimsJwt(claimsJwt);
+    public Jwt<UnprotectedHeader, Claims> parseClaimsJwt(String jwt) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
+        return this.jwtParser.parseClaimsJwt(jwt);
     }
 
     @Override
-    public Jws<String> parsePlaintextJws(String plaintextJws) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
-        return this.jwtParser.parsePlaintextJws(plaintextJws);
+    public Jws<byte[]> parseContentJws(String jws) throws UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
+        return this.jwtParser.parseContentJws(jws);
     }
 
     @Override
-    public Jws<Claims> parseClaimsJws(String claimsJws) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
-        return this.jwtParser.parseClaimsJws(claimsJws);
+    public Jws<Claims> parseClaimsJws(String jws) throws ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, SignatureException, IllegalArgumentException {
+        return this.jwtParser.parseClaimsJws(jws);
+    }
+
+    @Override
+    public Jwe<byte[]> parseContentJwe(String jwe) throws JwtException {
+        return this.jwtParser.parseContentJwe(jwe);
+    }
+
+    @Override
+    public Jwe<Claims> parseClaimsJwe(String jwe) throws JwtException {
+        return this.jwtParser.parseClaimsJwe(jwe);
     }
 }
