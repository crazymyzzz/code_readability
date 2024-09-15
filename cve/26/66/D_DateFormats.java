@@ -22,9 +22,14 @@ import java.util.Date;
 import java.util.TimeZone;
 
 /**
+ * Utility methods to format and parse date strings.
+ *
  * @since 0.10.0
  */
-public class DateFormats {
+public final class DateFormats {
+
+    private DateFormats() {
+    } // prevent instantiation
 
     private static final String ISO_8601_PATTERN = "yyyy-MM-dd'T'HH:mm:ss'Z'";
 
@@ -48,10 +53,25 @@ public class DateFormats {
         }
     };
 
+    /**
+     * Return an ISO-8601-formatted string with millisecond precision representing the
+     * specified {@code date}.
+     *
+     * @param date the date for which to create an ISO-8601-formatted string
+     * @return the date represented as an ISO-8601-formatted string with millisecond precision.
+     */
     public static String formatIso8601(Date date) {
         return formatIso8601(date, true);
     }
 
+    /**
+     * Returns an ISO-8601-formatted string with optional millisecond precision for the specified
+     * {@code date}.
+     *
+     * @param date          the date for which to create an ISO-8601-formatted string
+     * @param includeMillis whether to include millisecond notation within the string.
+     * @return the date represented as an ISO-8601-formatted string with optional millisecond precision.
+     */
     public static String formatIso8601(Date date, boolean includeMillis) {
         if (includeMillis) {
             return ISO_8601_MILLIS.get().format(date);
@@ -59,6 +79,14 @@ public class DateFormats {
         return ISO_8601.get().format(date);
     }
 
+    /**
+     * Parse the specified ISO-8601-formatted date string and return the corresponding {@link Date} instance.  The
+     * date string may optionally contain millisecond notation, and those milliseconds will be represented accordingly.
+     *
+     * @param s the ISO-8601-formatted string to parse
+     * @return the string's corresponding {@link Date} instance.
+     * @throws ParseException if the specified date string is not a validly-formatted ISO-8601 string.
+     */
     public static Date parseIso8601Date(String s) throws ParseException {
         Assert.notNull(s, "String argument cannot be null.");
         if (s.lastIndexOf('.') > -1) { //assume ISO-8601 with milliseconds
