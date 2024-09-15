@@ -364,8 +364,13 @@ public abstract class RealmBase extends LifecycleMBeanBase implements Realm {
         String md5a1 = getDigest(username, realm);
         if (md5a1 == null)
             return null;
-        String serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":"
-            + cnonce + ":" + qop + ":" + md5a2;
+        String serverDigestValue;
+        if (qop == null) {
+            serverDigestValue = md5a1 + ":" + nOnce + ":" + md5a2;
+        } else {
+            serverDigestValue = md5a1 + ":" + nOnce + ":" + nc + ":" +
+                    cnonce + ":" + qop + ":" + md5a2;
+        }
 
         byte[] valueBytes = null;
         if(getDigestEncoding() == null) {
