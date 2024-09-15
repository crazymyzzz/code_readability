@@ -47,6 +47,16 @@ public class FixedClock implements Clock {
         this.now = now;
     }
 
+    /**
+     * Creates a new fixed clock using the specified seed timestamp.  All calls to
+     * {@link #now now()} will always return this seed Date.
+     *
+     * @param timeInMillis the specified Date in milliseconds to always return from all calls to {@link #now now()}.
+     */
+    public FixedClock(long timeInMillis) {
+        this(new Date(timeInMillis));
+    }
+
     @Override
     public Date now() {
         return this.now;
