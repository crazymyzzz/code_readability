
@GwtIncompatible
public class AtomicDoubleArray implements java.io.Serializable {

  /** Reconstitutes the instance from a stream (that is, deserializes it). */
  private void readObject(java.io.ObjectInputStream s)
      throws java.io.IOException, ClassNotFoundException {
    s.defaultReadObject();

    int length = s.readInt();
    ImmutableLongArray.Builder builder = ImmutableLongArray.builder();
    for (int i = 0; i < length; i++) {
      builder.add(doubleToRawLongBits(s.readDouble()));
    }
    this.longs = new AtomicLongArray(builder.build().toArray());
  }
}
