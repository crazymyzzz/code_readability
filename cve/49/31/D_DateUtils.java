@@ -22,6 +22,8 @@ import java.text.SimpleDateFormat;
 import java.time.Instant;
 import java.time.LocalDate;
 import java.time.ZoneId;
+import java.time.format.DateTimeFormatter;
+import java.time.temporal.TemporalAccessor;
 import java.util.Calendar;
 import java.util.Date;
 import java.util.GregorianCalendar;
@@ -32,6 +34,7 @@ public class DateUtils {
      * Formats this date in the standard ISO yyyy-MM-dd HH:mm:ss format.
      *
      * @param date The date to format.
+     *
      * @return The date in ISO format. An empty string if the date is null.
      */
     public static String formatDateAsIsoString(Date date) {
@@ -41,10 +44,19 @@ public class DateUtils {
         return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
     }
 
+    public static String formatStringAsIsoDateString(String date) {
+        if (date == null) {
+            return "";
+        }
+        
+        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Date.from(Instant.from(DateTimeFormatter.ISO_INSTANT.parse(date))));
+    }
+
     /**
      * Formats the time of this date in the standard ISO HH:mm:ss format.
      *
      * @param date The date to format.
+     *
      * @return The time in ISO format. An empty string if the time is null.
      */
     public static String formatTimeAsIsoString(Date date) {
@@ -57,9 +69,10 @@ public class DateUtils {
     /**
      * Create a new date with this year, month and day.
      *
-     * @param year The year.
+     * @param year  The year.
      * @param month The month (1-12).
-     * @param day The day (1-31).
+     * @param day   The day (1-31).
+     *
      * @return The date.
      */
     public static Date toDate(int year, int month, int day) {
@@ -70,6 +83,7 @@ public class DateUtils {
      * Converts this date into a YYYY-MM-dd string.
      *
      * @param date The date.
+     *
      * @return The matching string.
      */
     public static String toDateString(Date date) {
