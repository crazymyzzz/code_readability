@@ -466,6 +466,11 @@ public class EllipticCurvesTest {
       JSONArray tests = group.getJSONArray("tests");
       for (int j = 0; j < tests.length(); j++) {
         JSONObject testcase = tests.getJSONObject(j);
+        if (WycheproofTestUtil.checkFlags(testcase, "CVE_2017_10176")) {
+          System.out.println("Skipping CVE-2017-10176 test, see b/73760761");
+          continue;
+        }
+
         String tcId =
             String.format(
                 "testcase %d (%s)", testcase.getInt("tcId"), testcase.getString("comment"));
@@ -485,13 +490,24 @@ public class EllipticCurvesTest {
           ECPublicKey pubKey = (ECPublicKey) kf.generatePublic(x509keySpec);
           String sharedSecret = Hex.encode(EllipticCurves.computeSharedSecret(privKey, pubKey));
           if (result.equals("invalid")) {
-            System.out.printf(
-                "FAIL %s: accepting invalid parameters, shared secret: %s%n", tcId, sharedSecret);
-            errors++;
+            if (expectedSharedSecret.equals(sharedSecret)
+                && WycheproofTestUtil.checkFlags(
+                    testcase, "WrongOrder", "WeakPublicKey", "UnnamedCurve")) {
+              System.out.println(
+                  tcId + " accepted invalid parameters but shared secret is correct.");
+            } else {
+              System.out.println(
+                  "FAIL " + tcId + " accepted invalid parameters, shared secret: " + sharedSecret);
+              errors++;
+            }
           } else if (!expectedSharedSecret.equals(sharedSecret)) {
-            System.out.printf(
-                "FAIL %s: incorrect shared secret, computed: %s, expected: %s%n",
-                tcId, sharedSecret, expectedSharedSecret);
+            System.out.println(
+                "FAIL "
+                    + tcId
+                    + " incorrect shared secret, computed: "
+                    + sharedSecret
+                    + " expected: "
+                    + expectedSharedSecret);
             errors++;
           }
         } catch (NoSuchAlgorithmException ex) {
@@ -501,12 +517,14 @@ public class EllipticCurvesTest {
           }
         } catch (GeneralSecurityException ex) {
           if (result.equals("valid")) {
-            System.out.printf("FAIL %s, exception %s%n", tcId, ex);
+            System.out.println("FAIL " + tcId + " exception: " + ex.toString());
+            ex.printStackTrace();
             errors++;
           }
         } catch (Exception ex) {
           // Other exceptions typically indicate that something is wrong with the implementation.
-          System.out.printf("FAIL %s, exception %s%n", tcId, ex);
+          System.out.println("FAIL " + tcId + " exception: " + ex.toString());
+          ex.printStackTrace();
           errors++;
         }
       }
