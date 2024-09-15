

/** Class for encoding and decoding ETC1 compressed images. Also provides methods to add a PKM header.
 * @author mzechner */
public class ETC1 {


		public ETC1Data (FileHandle pkmFile) {
			byte[] buffer = new byte[1024 * 10];
			DataInputStream in = null;
			try {
				in = new DataInputStream(new BufferedInputStream(new GZIPInputStream(pkmFile.read())));
				int fileSize = in.readInt();
				compressedData = BufferUtils.newUnsafeByteBuffer(fileSize);
				int readBytes = 0;
				while ((readBytes = in.read(buffer)) != -1) {
					compressedData.put(buffer, 0, readBytes);
				}
				((Buffer)compressedData).position(0);
				((Buffer)compressedData).limit(compressedData.capacity());
			} catch (Exception e) {
				throw new GdxRuntimeException("Couldn't load pkm file '" + pkmFile + "'", e);
			} finally {
				StreamUtils.closeQuietly(in);
			}

			width = getWidthPKM(compressedData, 0);
			height = getHeightPKM(compressedData, 0);
			dataOffset = PKM_HEADER_SIZE;
			((Buffer)compressedData).position(dataOffset);
			checkNPOT();
		}



		/** Writes the ETC1Data with a PKM header to the given file.
		 * @param file the file. */
		public void write (FileHandle file) {
			DataOutputStream write = null;
			byte[] buffer = new byte[10 * 1024];
			int writtenBytes = 0;
			((Buffer)compressedData).position(0);
			((Buffer)compressedData).limit(compressedData.capacity());
			try {
				write = new DataOutputStream(new GZIPOutputStream(file.write(false)));
				write.writeInt(compressedData.capacity());
				while (writtenBytes != compressedData.capacity()) {
					int bytesToWrite = Math.min(compressedData.remaining(), buffer.length);
					compressedData.get(buffer, 0, bytesToWrite);
					write.write(buffer, 0, bytesToWrite);
					writtenBytes += bytesToWrite;
				}
			} catch (Exception e) {
				throw new GdxRuntimeException("Couldn't write PKM file to '" + file + "'", e);
			} finally {
				StreamUtils.closeQuietly(write);
			}
			((Buffer)compressedData).position(dataOffset);
			((Buffer)compressedData).limit(compressedData.capacity());
		}



}
