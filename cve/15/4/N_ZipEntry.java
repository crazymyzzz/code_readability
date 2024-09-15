
class ZipEntry implements ZipConstants, Cloneable {


    /**
     * Sets the modification time of the entry.
     * @param time the entry modification time in number of milliseconds
     *             since the epoch
     * @see #getTime()
     */
    public void setTime(long time) {
        // fix for bug 6625963: we bypass time calculations while Kernel is
        // downloading bundles, since they aren't necessary and would cause
        // the Kernel core to depend upon the (very large) time zone data
        if (sun.misc.VM.isBootedKernelVM() &&
            sun.jkernel.DownloadManager.isCurrentThreadDownloading()) {
            this.time = sun.jkernel.DownloadManager.KERNEL_STATIC_MODTIME;
        } else {
            this.time = javaToDosTime(time);
        }
    }



    /**
     * Sets the optional comment string for the entry.
     *
     * <p>ZIP entry comments have maximum length of 0xffff. If the length of the
     * specified comment string is greater than 0xFFFF bytes after encoding, only
     * the first 0xFFFF bytes are output to the ZIP file entry.
     *
     * @param comment the comment string
     * @exception IllegalArgumentException if the length of the specified
     *            comment string is greater than 0xFFFF bytes
     * @see #getComment()
     */
    public void setComment(String comment) {
        if (comment != null && comment.length() > 0xffff) {
            throw new IllegalArgumentException("invalid entry comment length");
        }
        this.comment = comment;
    }

   
}
