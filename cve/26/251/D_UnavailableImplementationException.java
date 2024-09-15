@@ -17,13 +17,16 @@ package io.jsonwebtoken.impl.lang;
 
 /**
  * Exception indicating that no implementation of an jjwt-api SPI was found on the classpath.
+ *
  * @since 0.11.0
  */
 public final class UnavailableImplementationException extends RuntimeException {
 
-    private static final String DEFAULT_NOT_FOUND_MESSAGE = "Unable to find an implementation for %s using java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, for example jjwt-impl.jar, or your own .jar for custom implementations.";
+    private static final String DEFAULT_NOT_FOUND_MESSAGE = "Unable to find an implementation for %s using " +
+            "java.util.ServiceLoader. Ensure you include a backing implementation .jar in the classpath, " +
+            "for example jjwt-impl.jar, or your own .jar for custom implementations.";
 
-    UnavailableImplementationException(final Class klass) {
+    UnavailableImplementationException(final Class<?> klass) {
         super(String.format(DEFAULT_NOT_FOUND_MESSAGE, klass));
     }
 }
