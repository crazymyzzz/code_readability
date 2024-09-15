@@ -109,7 +109,15 @@ public String getName() {
      * @see #getTime()
      */
     public void setTime(long time) {
-        this.time = javaToDosTime(time);
+        // fix for bug 6625963: we bypass time calculations while Kernel is
+        // downloading bundles, since they aren't necessary and would cause
+        // the Kernel core to depend upon the (very large) time zone data
+        if (sun.misc.VM.isBootedKernelVM() &&
+            sun.jkernel.DownloadManager.isCurrentThreadDownloading()) {
+            this.time = sun.jkernel.DownloadManager.KERNEL_STATIC_MODTIME;
+        } else {
+            this.time = javaToDosTime(time);
+        }
     }
 
     /**
@@ -245,10 +253,14 @@ public byte[] getExtra() {
      * the first 0xFFFF bytes are output to the ZIP file entry.
      *
      * @param comment the comment string
-     *
+     * @exception IllegalArgumentException if the length of the specified
+     *            comment string is greater than 0xFFFF bytes
      * @see #getComment()
      */
     public void setComment(String comment) {
+        if (comment != null && comment.length() > 0xffff) {
+            throw new IllegalArgumentException("invalid entry comment length");
+        }
         this.comment = comment;
     }
 
