@@ -92,7 +92,7 @@ public final class UDecoder {
             idx=idx2;
         }
 
-        boolean noSlash = !(ALLOW_ENCODED_SLASH || query);
+        final boolean noSlash = !(ALLOW_ENCODED_SLASH || query);
 
         for( int j=idx; j<end; j++, idx++ ) {
             if( buff[ j ] == '+' && query) {
@@ -160,6 +160,8 @@ public final class UDecoder {
             idx=idx2;
         }
 
+        final boolean noSlash = !(ALLOW_ENCODED_SLASH || query);
+
         for( int j=idx; j<cend; j++, idx++ ) {
             if( buff[ j ] == '+' && query ) {
                 buff[idx]=( ' ' );
@@ -179,6 +181,9 @@ public final class UDecoder {
 
                 j+=2;
                 int res=x2c( b1, b2 );
+                if (noSlash && (res == '/')) {
+                    throw EXCEPTION_SLASH;
+                }
                 buff[idx]=(char)res;
             }
         }
@@ -206,7 +211,11 @@ public final class UDecoder {
             if( strValue==null ) {
                 return;
             }
-            mb.setString( convert( strValue, query ));
+            try {
+                mb.setString( convert( strValue, query ));
+            } catch (RuntimeException ex) {
+                throw new DecodeException(ex.getMessage());
+            }
             break;
         case MessageBytes.T_CHARS:
             CharChunk charC=mb.getCharChunk();
@@ -236,6 +245,8 @@ public final class UDecoder {
             return str;
         }
 
+        final boolean noSlash = !(ALLOW_ENCODED_SLASH || query);
+
         StringBuilder dec = new StringBuilder();    // decoded string output
         int strPos = 0;
         int strLen = str.length();
@@ -273,8 +284,12 @@ public final class UDecoder {
                 // We throw the original exception - the super will deal with
                 // it
                 //                try {
-                dec.append((char)Integer.
-                           parseInt(str.substring(strPos + 1, strPos + 3),16));
+                char res = (char) Integer.parseInt(
+                        str.substring(strPos + 1, strPos + 3), 16);
+                if (noSlash && (res == '/')) {
+                    throw new IllegalArgumentException("noSlash");
+                }
+                dec.append(res);
                 strPos += 3;
             }
         }
