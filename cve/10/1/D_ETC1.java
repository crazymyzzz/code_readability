/**
 * @@ -71,8 +71,8
 * @@ -82,7 +82,7
 * 为同一个函数
 */
@@ -71,8 +71,8 @@ public class ETC1 {
 				while ((readBytes = in.read(buffer)) != -1) {
 					compressedData.put(buffer, 0, readBytes);
 				}
-				((Buffer)compressedData).position(0);
-				((Buffer)compressedData).limit(compressedData.capacity());
+				((Buffer) compressedData).position(0);
+				((Buffer) compressedData).limit(compressedData.capacity());
 			} catch (Exception e) {
 				throw new GdxRuntimeException("Couldn't load pkm file '" + pkmFile + "'", e);
 			} finally {
@@ -82,7 +82,7 @@ public class ETC1 {
 			width = getWidthPKM(compressedData, 0);
 			height = getHeightPKM(compressedData, 0);
 			dataOffset = PKM_HEADER_SIZE;
-			((Buffer)compressedData).position(dataOffset);
+			((Buffer) compressedData).position(dataOffset);
 			checkNPOT();
 		}
 
/**
 * @@ -103,8 +103,8
 * @@ -119,8 +119,8 
 * 为同一个函数
 */
@@ -103,8 +103,8 @@ public class ETC1 {
 			DataOutputStream write = null;
 			byte[] buffer = new byte[10 * 1024];
 			int writtenBytes = 0;
-			((Buffer)compressedData).position(0);
-			((Buffer)compressedData).limit(compressedData.capacity());
+			((Buffer) compressedData).position(0);
+			((Buffer) compressedData).limit(compressedData.capacity());
 			try {
 				write = new DataOutputStream(new GZIPOutputStream(file.write(false)));
 				write.writeInt(compressedData.capacity());
@@ -119,8 +119,8 @@ public class ETC1 {
 			} finally {
 				StreamUtils.closeQuietly(write);
 			}
-			((Buffer)compressedData).position(dataOffset);
-			((Buffer)compressedData).limit(compressedData.capacity());
+			((Buffer) compressedData).position(dataOffset);
+			((Buffer) compressedData).limit(compressedData.capacity());
 		}
 
 		/** Releases the native resources of the ETC1Data instance. */

