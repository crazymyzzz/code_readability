
class ZipEntry implements ZipConstants, Cloneable {


    /**
     * Sets the modification time of the entry.
     * @param time the entry modification time in number of milliseconds
     *             since the epoch
     * @see #getTime()
     */
    public void setTime(long time) {
        this.time = javaToDosTime(time);
    }



    /**
     * Sets the optional comment string for the entry.
     *
     * <p>ZIP entry comments have maximum length of 0xffff. If the length of the
     * specified comment string is greater than 0xFFFF bytes after encoding, only
     * the first 0xFFFF bytes are output to the ZIP file entry.
     *
     * @param comment the comment string
     *
     * @see #getComment()
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    
}
