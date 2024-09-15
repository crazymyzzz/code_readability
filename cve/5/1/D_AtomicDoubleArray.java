
@@ -247,13 +248,11 @@ public class AtomicDoubleArray implements java.io.Serializable {
       throws java.io.IOException, ClassNotFoundException {
     s.defaultReadObject();
 
-    // Read in array length and allocate array
     int length = s.readInt();
-    this.longs = new AtomicLongArray(length);
-
-    // Read in all elements in the proper order.
+    ImmutableLongArray.Builder builder = ImmutableLongArray.builder();
     for (int i = 0; i < length; i++) {
-      set(i, s.readDouble());
+      builder.add(doubleToRawLongBits(s.readDouble()));
     }
+    this.longs = new AtomicLongArray(builder.build().toArray());
   }
 }
