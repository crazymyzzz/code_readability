@@ -16,6 +16,7 @@ package io.trino.tests;
 import org.testng.annotations.Test;
 
 import java.time.ZoneId;
+import java.util.Locale;
 
 import static org.testng.Assert.assertEquals;
 
@@ -27,4 +28,11 @@ public class TestVerifyTrinoTestsTestSetup
         // Ensure that the zone defined in the POM is correctly set in the test JVM
         assertEquals(ZoneId.systemDefault().getId(), "America/Bahia_Banderas");
     }
+
+    @Test
+    public void testJvmLocale()
+    {
+        // Ensure that locale defined in the POM is correctly set in the test JVM
+        assertEquals(Locale.getDefault(), Locale.US);
+    }
 }
