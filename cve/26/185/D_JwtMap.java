@@ -15,161 +15,188 @@
  */
 package io.jsonwebtoken.impl;
 
+import io.jsonwebtoken.impl.lang.Field;
+import io.jsonwebtoken.impl.lang.FieldReadable;
+import io.jsonwebtoken.impl.lang.Nameable;
+import io.jsonwebtoken.impl.lang.RedactedSupplier;
 import io.jsonwebtoken.lang.Assert;
-import io.jsonwebtoken.lang.DateFormats;
+import io.jsonwebtoken.lang.Collections;
+import io.jsonwebtoken.lang.Objects;
+import io.jsonwebtoken.lang.Strings;
 
-import java.text.ParseException;
-import java.util.Calendar;
+import java.lang.reflect.Array;
 import java.util.Collection;
-import java.util.Date;
 import java.util.LinkedHashMap;
 import java.util.Map;
 import java.util.Set;
 
-public class JwtMap implements Map<String, Object> {
+public class JwtMap implements Map<String, Object>, FieldReadable, Nameable {
 
-    private final Map<String, Object> map;
+    protected final Map<String, Field<?>> FIELDS;
+    protected final Map<String, Object> values; // canonical values formatted per RFC requirements
+    protected final Map<String, Object> idiomaticValues; // the values map with any RFC values converted to Java type-safe values where possible
 
-    public JwtMap() {
-        this.map = new LinkedHashMap<>();
-    }
-
-    public JwtMap(Map<String, ?> map) {
-        this();
-        Assert.notNull(map, "Map argument cannot be null.");
-        putAll(map);
+    public JwtMap(Set<Field<?>> fieldSet) {
+        Assert.notEmpty(fieldSet, "Fields cannot be null or empty.");
+        Map<String, Field<?>> fields = new LinkedHashMap<>();
+        for (Field<?> field : fieldSet) {
+            fields.put(field.getId(), field);
+        }
+        this.FIELDS = java.util.Collections.unmodifiableMap(fields);
+        this.values = new LinkedHashMap<>();
+        this.idiomaticValues = new LinkedHashMap<>();
     }
 
-    protected String getString(String name) {
-        Object v = get(name);
-        return v != null ? String.valueOf(v) : null;
+    public JwtMap(Set<Field<?>> fieldSet, Map<String, ?> values) {
+        this(fieldSet);
+        Assert.notNull(values, "Map argument cannot be null.");
+        putAll(values);
     }
 
-    protected static Date toDate(Object v, String name) {
-        if (v == null) {
-            return null;
-        } else if (v instanceof Date) {
-            return (Date) v;
-        } else if (v instanceof Calendar) { //since 0.10.0
-            return ((Calendar) v).getTime();
-        } else if (v instanceof Number) {
-            //assume millis:
-            long millis = ((Number) v).longValue();
-            return new Date(millis);
-        } else if (v instanceof String) {
-            return parseIso8601Date((String) v, name); //ISO-8601 parsing since 0.10.0
-        } else {
-            throw new IllegalStateException("Cannot create Date from '" + name + "' value '" + v + "'.");
-        }
-    }
-
-    // @since 0.10.0
-    private static Date parseIso8601Date(String s, String name) throws IllegalArgumentException {
-        try {
-            return DateFormats.parseIso8601Date(s);
-        } catch (ParseException e) {
-            String msg = "'" + name + "' value does not appear to be ISO-8601-formatted: " + s;
-            throw new IllegalArgumentException(msg, e);
-        }
+    @Override
+    public String getName() {
+        return "Map";
     }
 
-    // @since 0.10.0
-    protected static Date toSpecDate(Object v, String name) {
-        if (v == null) {
-            return null;
-        } else if (v instanceof Number) {
-            // https://github.com/jwtk/jjwt/issues/122:
-            // The JWT RFC *mandates* NumericDate values are represented as seconds.
-            // Because java.util.Date requires milliseconds, we need to multiply by 1000:
-            long seconds = ((Number) v).longValue();
-            v = seconds * 1000;
-        } else if (v instanceof String) {
-            // https://github.com/jwtk/jjwt/issues/122
-            // The JWT RFC *mandates* NumericDate values are represented as seconds.
-            // Because java.util.Date requires milliseconds, we need to multiply by 1000:
-            try {
-                long seconds = Long.parseLong((String) v);
-                v = seconds * 1000;
-            } catch (NumberFormatException ignored) {
-            }
-        }
-        //v would have been normalized to milliseconds if it was a number value, so perform normal date conversion:
-        return toDate(v, name);
+    public static boolean isReducibleToNull(Object v) {
+        return v == null ||
+                (v instanceof String && !Strings.hasText((String) v)) ||
+                (v instanceof Collection && Collections.isEmpty((Collection<?>) v)) ||
+                (v instanceof Map && Collections.isEmpty((Map<?, ?>) v)) ||
+                (v.getClass().isArray() && Array.getLength(v) == 0);
     }
 
-    protected void setValue(String name, Object v) {
-        if (v == null) {
-            map.remove(name);
-        } else {
-            map.put(name, v);
-        }
+    protected Object idiomaticGet(String key) {
+        return this.idiomaticValues.get(key);
     }
 
-    @Deprecated //remove just before 1.0.0
-    protected void setDate(String name, Date d) {
-        if (d == null) {
-            map.remove(name);
-        } else {
-            long seconds = d.getTime() / 1000;
-            map.put(name, seconds);
-        }
+    protected <T> T idiomaticGet(Field<T> field) {
+        Object value = this.idiomaticValues.get(field.getId());
+        return field.cast(value);
     }
 
-    protected Object setDateAsSeconds(String name, Date d) {
-        if (d == null) {
-            return map.remove(name);
-        } else {
-            long seconds = d.getTime() / 1000;
-            return map.put(name, seconds);
-        }
+    @Override
+    public <T> T get(Field<T> field) {
+        Assert.notNull(field, "Field cannot be null.");
+        final String id = Assert.hasText(field.getId(), "Field id cannot be null or empty.");
+        Object value = idiomaticValues.get(id);
+        return field.cast(value);
     }
 
     @Override
     public int size() {
-        return map.size();
+        return values.size();
     }
 
     @Override
     public boolean isEmpty() {
-        return map.isEmpty();
+        return values.isEmpty();
     }
 
     @Override
     public boolean containsKey(Object o) {
-        return map.containsKey(o);
+        return values.containsKey(o);
     }
 
     @Override
     public boolean containsValue(Object o) {
-        return map.containsValue(o);
+        return values.containsValue(o);
     }
 
     @Override
     public Object get(Object o) {
-        return map.get(o);
+        return values.get(o);
+    }
+
+    /**
+     * Convenience method to put a value for a canonical field.
+     *
+     * @param field the field representing the property name to set
+     * @param value the value to set
+     * @return the previous value for the field name, or {@code null} if there was no previous value
+     * @since JJWT_RELEASE_VERSION
+     */
+    protected Object put(Field<?> field, Object value) {
+        return put(field.getId(), value);
     }
 
     @Override
-    public Object put(String s, Object o) {
-        if (o == null) {
-            return map.remove(s);
+    public Object put(String name, Object value) {
+        name = Assert.notNull(Strings.clean(name), "Member name cannot be null or empty.");
+        if (value instanceof String) {
+            value = Strings.clean((String) value);
+        }
+        return idiomaticPut(name, value);
+    }
+
+    // ensures that if a property name matches an RFC-specified name, that value can be represented
+    // as an idiomatic type-safe Java value in addition to the canonical RFC/encoded value.
+    private Object idiomaticPut(String name, Object value) {
+        Assert.stateNotNull(name, "Name cannot be null."); // asserted by caller
+        Field<?> field = FIELDS.get(name);
+        if (field != null) { //Setting a JWA-standard property - let's ensure we can represent it idiomatically:
+            return apply(field, value);
+        } else { //non-standard/custom property:
+            return nullSafePut(name, value);
+        }
+    }
+
+    protected Object nullSafePut(String name, Object value) {
+        if (isReducibleToNull(value)) {
+            return remove(name);
         } else {
-            return map.put(s, o);
+            this.idiomaticValues.put(name, value);
+            return this.values.put(name, value);
+        }
+    }
+
+    protected <T> Object apply(Field<T> field, Object rawValue) {
+
+        final String id = field.getId();
+
+        if (isReducibleToNull(rawValue)) {
+            return remove(id);
+        }
+
+        T idiomaticValue; // preferred Java format
+        Object canonicalValue; // as required by the RFC
+        try {
+            idiomaticValue = field.applyFrom(rawValue);
+            Assert.notNull(idiomaticValue, "Field's resulting idiomaticValue cannot be null.");
+            canonicalValue = field.applyTo(idiomaticValue);
+            Assert.notNull(canonicalValue, "Field's resulting canonicalValue cannot be null.");
+        } catch (Exception e) {
+            StringBuilder sb = new StringBuilder(100);
+            sb.append("Invalid ").append(getName()).append(" ").append(field).append(" value");
+            if (field.isSecret()) {
+                sb.append(": ").append(RedactedSupplier.REDACTED_VALUE);
+            } else if (!(rawValue instanceof byte[])) {
+                // don't print raw byte array gibberish.  We can't base64[url] encode it either because that could
+                // make the exception message confusing: the developer would see an encoded string and could think
+                // that was the rawValue specified when it wasn't.
+                sb.append(": ").append(Objects.nullSafeToString(rawValue));
+            }
+            sb.append(". ").append(e.getMessage());
+            String msg = sb.toString();
+            throw new IllegalArgumentException(msg, e);
         }
+        Object retval = nullSafePut(id, canonicalValue);
+        this.idiomaticValues.put(id, idiomaticValue);
+        return retval;
     }
 
     @Override
-    public Object remove(Object o) {
-        return map.remove(o);
+    public Object remove(Object key) {
+        this.idiomaticValues.remove(key);
+        return this.values.remove(key);
     }
 
-    @SuppressWarnings("NullableProblems")
     @Override
     public void putAll(Map<? extends String, ?> m) {
         if (m == null) {
             return;
         }
-        for (Map.Entry <? extends String, ?>entry : m.entrySet()) {
+        for (Map.Entry<? extends String, ?> entry : m.entrySet()) {
             String s = entry.getKey();
             put(s, entry.getValue());
         }
@@ -177,36 +204,38 @@ public class JwtMap implements Map<String, Object> {
 
     @Override
     public void clear() {
-        map.clear();
+        this.values.clear();
+        this.idiomaticValues.clear();
     }
 
     @Override
     public Set<String> keySet() {
-        return map.keySet();
+        return values.keySet();
     }
 
     @Override
     public Collection<Object> values() {
-        return map.values();
+        return values.values();
     }
 
     @Override
     public Set<Entry<String, Object>> entrySet() {
-        return map.entrySet();
+        return values.entrySet();
     }
 
     @Override
     public String toString() {
-        return map.toString();
+        return values.toString();
     }
 
     @Override
     public int hashCode() {
-        return map.hashCode();
+        return values.hashCode();
     }
 
+    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
     @Override
     public boolean equals(Object obj) {
-        return map.equals(obj);
+        return values.equals(obj);
     }
 }
