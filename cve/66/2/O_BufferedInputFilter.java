

/**
 * Input filter responsible for reading and buffering the request body, so that
 * it does not interfere with client SSL handshake messages.
 */
public class BufferedInputFilter implements InputFilter {



    public void recycle() {
        if (buffered.getBuffer().length > 65536) {
            buffered = null;
        } else {
            buffered.recycle();
        }
        tempRead.recycle();
        hasRead = false;
        buffer = null;
    }

    
}
