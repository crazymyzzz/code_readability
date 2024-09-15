@@ -17,8 +17,15 @@ package io.jsonwebtoken.impl;
 
 import io.jsonwebtoken.Claims;
 import io.jsonwebtoken.RequiredTypeException;
+import io.jsonwebtoken.impl.lang.Field;
+import io.jsonwebtoken.impl.lang.Fields;
+import io.jsonwebtoken.impl.lang.JwtDateConverter;
+import io.jsonwebtoken.lang.Assert;
+import io.jsonwebtoken.lang.Collections;
+
 import java.util.Date;
 import java.util.Map;
+import java.util.Set;
 
 public class DefaultClaims extends JwtMap implements Claims {
 
@@ -30,141 +37,156 @@ public class DefaultClaims extends JwtMap implements Claims {
             "See https://github.com/jwtk/jjwt#custom-json-processor for more information. If using Jackson, you can " +
             "specify custom claim POJO types as described in https://github.com/jwtk/jjwt#json-jackson-custom-types";
 
+    static final Field<String> ISSUER = Fields.string(Claims.ISSUER, "Issuer");
+    static final Field<String> SUBJECT = Fields.string(Claims.SUBJECT, "Subject");
+    static final Field<String> AUDIENCE = Fields.string(Claims.AUDIENCE, "Audience");
+    static final Field<Date> EXPIRATION = Fields.rfcDate(Claims.EXPIRATION, "Expiration Time");
+    static final Field<Date> NOT_BEFORE = Fields.rfcDate(Claims.NOT_BEFORE, "Not Before");
+    static final Field<Date> ISSUED_AT = Fields.rfcDate(Claims.ISSUED_AT, "Issued At");
+    static final Field<String> JTI = Fields.string(Claims.ID, "JWT ID");
+
+    static final Set<Field<?>> FIELDS = Collections.<Field<?>>setOf(
+            ISSUER, SUBJECT, AUDIENCE, EXPIRATION, NOT_BEFORE, ISSUED_AT, JTI
+    );
+
     public DefaultClaims() {
-        super();
+        super(FIELDS);
     }
 
     public DefaultClaims(Map<String, ?> map) {
-        super(map);
+        super(FIELDS, map);
+    }
+
+    @Override
+    public String getName() {
+        return "JWT Claim";
     }
 
     @Override
     public String getIssuer() {
-        return getString(ISSUER);
+        return idiomaticGet(ISSUER);
     }
 
     @Override
     public Claims setIssuer(String iss) {
-        setValue(ISSUER, iss);
+        put(ISSUER, iss);
         return this;
     }
 
     @Override
     public String getSubject() {
-        return getString(SUBJECT);
+        return idiomaticGet(SUBJECT);
     }
 
     @Override
     public Claims setSubject(String sub) {
-        setValue(SUBJECT, sub);
+        put(SUBJECT, sub);
         return this;
     }
 
     @Override
     public String getAudience() {
-        return getString(AUDIENCE);
+        return idiomaticGet(AUDIENCE);
     }
 
     @Override
     public Claims setAudience(String aud) {
-        setValue(AUDIENCE, aud);
+        put(AUDIENCE, aud);
         return this;
     }
 
     @Override
     public Date getExpiration() {
-        return get(Claims.EXPIRATION, Date.class);
+        return idiomaticGet(EXPIRATION);
     }
 
     @Override
     public Claims setExpiration(Date exp) {
-        setDateAsSeconds(Claims.EXPIRATION, exp);
+        put(EXPIRATION, exp);
         return this;
     }
 
     @Override
     public Date getNotBefore() {
-        return get(Claims.NOT_BEFORE, Date.class);
+        return idiomaticGet(NOT_BEFORE);
     }
 
     @Override
     public Claims setNotBefore(Date nbf) {
-        setDateAsSeconds(Claims.NOT_BEFORE, nbf);
+        put(NOT_BEFORE, nbf);
         return this;
     }
 
     @Override
     public Date getIssuedAt() {
-        return get(Claims.ISSUED_AT, Date.class);
+        return idiomaticGet(ISSUED_AT);
     }
 
     @Override
     public Claims setIssuedAt(Date iat) {
-        setDateAsSeconds(Claims.ISSUED_AT, iat);
+        put(ISSUED_AT, iat);
         return this;
     }
 
     @Override
     public String getId() {
-        return getString(ID);
+        return idiomaticGet(JTI);
     }
 
     @Override
     public Claims setId(String jti) {
-        setValue(Claims.ID, jti);
+        put(JTI, jti);
         return this;
     }
 
-    /**
-     * @since 0.10.0
-     */
-    private static boolean isSpecDate(String claimName) {
-        return Claims.EXPIRATION.equals(claimName) ||
-            Claims.ISSUED_AT.equals(claimName) ||
-            Claims.NOT_BEFORE.equals(claimName);
-    }
-
-    @Override
-    public Object put(String s, Object o) {
-        if (o instanceof Date && isSpecDate(s)) { //since 0.10.0
-            Date date = (Date)o;
-            return setDateAsSeconds(s, date);
-        }
-        return super.put(s, o);
-    }
-
     @Override
     public <T> T get(String claimName, Class<T> requiredType) {
+        Assert.notNull(requiredType, "requiredType argument cannot be null.");
 
-        Object value = get(claimName);
+        Object value = idiomaticGet(claimName);
+        if (requiredType.isInstance(value)) {
+            return requiredType.cast(value);
+        }
+
+        value = get(claimName);
         if (value == null) {
             return null;
         }
 
         if (Date.class.equals(requiredType)) {
-            if (isSpecDate(claimName)) {
-                value = toSpecDate(value, claimName);
-            } else {
-                value = toDate(value, claimName);
+            try {
+                value = JwtDateConverter.toDate(value); // NOT specDate logic
+            } catch (Exception e) {
+                String msg = "Cannot create Date from '" + claimName + "' value '" + value + "'. Cause: " + e.getMessage();
+                throw new IllegalArgumentException(msg, e);
             }
         }
 
-        return castClaimValue(value, requiredType);
+        return castClaimValue(claimName, value, requiredType);
     }
 
-    private <T> T castClaimValue(Object value, Class<T> requiredType) {
+    private <T> T castClaimValue(String name, Object value, Class<T> requiredType) {
 
-        if (value instanceof Integer) {
-            int intValue = (Integer) value;
-            if (requiredType == Long.class) {
-                value = (long) intValue;
-            } else if (requiredType == Short.class && Short.MIN_VALUE <= intValue && intValue <= Short.MAX_VALUE) {
-                value = (short) intValue;
-            } else if (requiredType == Byte.class && Byte.MIN_VALUE <= intValue && intValue <= Byte.MAX_VALUE) {
-                value = (byte) intValue;
+        if (value instanceof Long || value instanceof Integer || value instanceof Short || value instanceof Byte) {
+            long longValue = ((Number) value).longValue();
+            if (Long.class.equals(requiredType)) {
+                value = longValue;
+            } else if (Integer.class.equals(requiredType) && Integer.MIN_VALUE <= longValue && longValue <= Integer.MAX_VALUE) {
+                value = (int) longValue;
+            } else if (requiredType == Short.class && Short.MIN_VALUE <= longValue && longValue <= Short.MAX_VALUE) {
+                value = (short) longValue;
+            } else if (requiredType == Byte.class && Byte.MIN_VALUE <= longValue && longValue <= Byte.MAX_VALUE) {
+                value = (byte) longValue;
             }
         }
 
+        if (value instanceof Long &&
+                (requiredType.equals(Integer.class) || requiredType.equals(Short.class) || requiredType.equals(Byte.class))) {
+            String msg = "Claim '" + name + "' value is too large or too small to be represented as a " +
+                    requiredType.getName() + " instance (would cause numeric overflow).";
+            throw new RequiredTypeException(msg);
+        }
+
         if (!requiredType.isInstance(value)) {
             throw new RequiredTypeException(String.format(CONVERSION_ERROR_MSG, value.getClass(), requiredType));
         }
