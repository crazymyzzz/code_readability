

/** Utility functions and enums for elliptic curve crypto, used in ECDSA and ECDH. */
public final class EllipticCurves {


  /** Checks that the public key's params spec is the same as the private key's params spec. */
  static void validatePublicKeySpec(ECPublicKey publicKey, ECPrivateKey privateKey)
      throws GeneralSecurityException {
    ECParameterSpec publicKeySpec = publicKey.getParams();
    ECParameterSpec privateKeySpec = privateKey.getParams();
    if (!publicKeySpec.getCurve().equals(privateKeySpec.getCurve())
        || !publicKeySpec.getGenerator().equals(privateKeySpec.getGenerator())
        || !publicKeySpec.getOrder().equals(privateKeySpec.getOrder())
        || publicKeySpec.getCofactor() != privateKeySpec.getCofactor()) {
      throw new GeneralSecurityException("invalid public key spec");
    }
  }

  /**
   * Returns the modulus of the field used by the curve specified in ecParams.
   *
   * @param curve must be a prime order elliptic curve
   * @return the order of the finite field over which curve is defined.
   */
  public static BigInteger getModulus(EllipticCurve curve) throws GeneralSecurityException {
    java.security.spec.ECField field = curve.getField();
    if (field instanceof java.security.spec.ECFieldFp) {
      return ((java.security.spec.ECFieldFp) field).getP();
    } else {
      throw new GeneralSecurityException("Only curves over prime order fields are supported");
    }
  }

  /**
   * Returns the size of an element of the field over which the curve is defined.
   *
   * @param curve must be a prime order elliptic curve
   * @return the size of an element in bits
   */
  private static int fieldSizeInBits(EllipticCurve curve) throws GeneralSecurityException {
    return getModulus(curve).subtract(BigInteger.ONE).bitLength();
  }

  /**
   * Returns the size of an element of the field over which the curve is defined.
   *
   * @param curve must be a prime order elliptic curve
   * @return the size of an element in bytes.
   */
  public static int fieldSizeInBytes(EllipticCurve curve) throws GeneralSecurityException {
    return (fieldSizeInBits(curve) + 7) / 8;
  }

  private static ECParameterSpec getNistCurveSpec(
      String decimalP, String decimalN, String hexB, String hexGX, String hexGY) {
    final BigInteger p = new BigInteger(decimalP);
    final BigInteger n = new BigInteger(decimalN);
    final BigInteger three = new BigInteger("3");
    final BigInteger a = p.subtract(three);
    final BigInteger b = new BigInteger(hexB, 16);
    final BigInteger gx = new BigInteger(hexGX, 16);
    final BigInteger gy = new BigInteger(hexGY, 16);
    final int h = 1;
    ECFieldFp fp = new ECFieldFp(p);
    java.security.spec.EllipticCurve curveSpec = new java.security.spec.EllipticCurve(fp, a, b);
    ECPoint g = new ECPoint(gx, gy);
    ECParameterSpec ecSpec = new ECParameterSpec(curveSpec, g, n, h);
    return ecSpec;
  }

  /**
   * Computes a square root modulo an odd prime. Timing and exceptions can leak information about
   * the inputs. Therefore this method must only be used to decompress public keys.
   *
   * @param x the square
   * @param p the prime modulus (the behaviour of the method is undefined if p is not prime).
   * @return a value s such that s^2 mod p == x mod p
   * @throws GeneralSecurityException if the square root could not be found.
   */
  protected static BigInteger modSqrt(BigInteger x, BigInteger p) throws GeneralSecurityException {
    if (p.signum() != 1) {
      throw new InvalidAlgorithmParameterException("p must be positive");
    }
    x = x.mod(p);
    BigInteger squareRoot = null;
    // Special case for x == 0.
    // This check is necessary for Cipolla's algorithm.
    if (x.equals(BigInteger.ZERO)) {
      return BigInteger.ZERO;
    }
    if (p.testBit(0) && p.testBit(1)) {
      // Case p % 4 == 3
      // q = (p + 1) / 4
      BigInteger q = p.add(BigInteger.ONE).shiftRight(2);
      squareRoot = x.modPow(q, p);
    } else if (p.testBit(0) && !p.testBit(1)) {
      // Case p % 4 == 1
      // For this case we use Cipolla's algorithm.
      // This alogorithm is preferrable to Tonelli-Shanks for primes p where p-1 is divisible by
      // a large power of 2, which is a frequent choice since it simplifies modular reduction.
      BigInteger a = BigInteger.ONE;
      BigInteger d = null;
      BigInteger q1 = p.subtract(BigInteger.ONE).shiftRight(1);
      int tries = 0;
      while (true) {
        d = a.multiply(a).subtract(x).mod(p);
        // Special case d==0. We need d!=0 below.
        if (d.equals(BigInteger.ZERO)) {
          return a;
        }
        // Computes the Legendre symbol. Using the Jacobi symbol would be a faster.
        BigInteger t = d.modPow(q1, p);
        if (t.add(BigInteger.ONE).equals(p)) {
          // d is a quadratic non-residue.
          break;
        } else if (!t.equals(BigInteger.ONE)) {
          // p does not divide d. Hence, t != 1 implies that p is not a prime.
          throw new InvalidAlgorithmParameterException("p is not prime");
        } else {
          a = a.add(BigInteger.ONE);
        }
        tries++;
        // If 128 tries were not enough to find a quadratic non-residue, then it is likely that
        // p is not prime. To avoid an infinite loop in this case we perform a primality test.
        // If p is prime then this test will be done with a negligible probability of 2^{-128}.
        if (tries == 128) {
          if (!p.isProbablePrime(80)) {
            throw new InvalidAlgorithmParameterException("p is not prime");
          }
        }
      }
      // Since d = a^2 - x is a quadratic non-residue modulo p, we have
      //   a - sqrt(d) == (a + sqrt(d))^p (mod p),
      // and hence
      //   x == (a + sqrt(d))(a - sqrt(d)) == (a + sqrt(d))^(p+1) (mod p).
      // Thus if x is square then (a + sqrt(d))^((p+1)/2) (mod p) is a square root of x.
      BigInteger q = p.add(BigInteger.ONE).shiftRight(1);
      BigInteger u = a;
      BigInteger v = BigInteger.ONE;
      for (int bit = q.bitLength() - 2; bit >= 0; bit--) {
        // Square u + v sqrt(d) and reduce mod p.
        BigInteger tmp = u.multiply(v);
        u = u.multiply(u).add(v.multiply(v).mod(p).multiply(d)).mod(p);
        v = tmp.add(tmp).mod(p);
        if (q.testBit(bit)) {
          // Multiply u + v sqrt(d) by a + sqrt(d) and reduce mod p.
          tmp = u.multiply(a).add(v.multiply(d)).mod(p);
          v = a.multiply(v).add(u).mod(p);
          u = tmp;
        }
      }
      squareRoot = u;
    }
    // The methods used to compute the square root only guarantees a correct result if the
    // preconditions (i.e. p prime and x is a square) are satisfied. Otherwise the value is
    // undefined. Hence it is important to verify that squareRoot is indeed a square root.
    if (squareRoot != null && squareRoot.multiply(squareRoot).mod(p).compareTo(x) != 0) {
      throw new GeneralSecurityException("Could not find a modular square root");
    }
    return squareRoot;
  }

  /**
   * Computes the y coordinate of a point on an elliptic curve. This method can be used to
   * decompress elliptic curve points.
   *
   * @param x the x-coordinate of the point
   * @param lsb the least significant bit of the y-coordinate of the point.
   * @param curve this must be an elliptic curve over a prime field using Weierstrass
   *     representation.
   * @return the y coordinate.
   * @throws GeneralSecurityException if there is no point with coordinate x on the curve, or if
   *     curve is not supported.
   */
  public static BigInteger getY(BigInteger x, boolean lsb, EllipticCurve curve)
      throws GeneralSecurityException {
    BigInteger p = getModulus(curve);
    BigInteger a = curve.getA();
    BigInteger b = curve.getB();
    BigInteger rhs = x.multiply(x).add(a).multiply(x).add(b).mod(p);
    BigInteger y = modSqrt(rhs, p);
    if (lsb != y.testBit(0)) {
      y = p.subtract(y).mod(p);
    }
    return y;
  }

  /**
   * Returns the encoding size of a point on an elliptic curve.
   *
   * @param curve the elliptic curve
   * @param format the format used to encode the point
   * @return the size of an encoded point in bytes
   * @throws GeneralSecurityException if the point format is unknown or if the elliptic curve is not
   *     supported
   */
  public static int encodingSizeInBytes(EllipticCurve curve, PointFormatType format)
      throws GeneralSecurityException {
    int coordinateSize = fieldSizeInBytes(curve);
    switch (format) {
      case UNCOMPRESSED:
        return 2 * coordinateSize + 1;
      case COMPRESSED:
        return coordinateSize + 1;
      default:
        throw new GeneralSecurityException("unknown EC point format");
    }
  }

  /**
   * Decodes an encoded point on an elliptic curve. This method checks that the encoded point is on
   * the curve.
   *
   * @param curve the elliptic curve
   * @param format the format used to enocde the point
   * @param encoded the encoded point
   * @return the point
   * @throws GeneralSecurityException if the encoded point is invalid or if the curve or format are
   *     not supported.
   * @deprecated use {#pointDecode}
   */
  @Deprecated
  public static ECPoint ecPointDecode(EllipticCurve curve, PointFormatType format, byte[] encoded)
      throws GeneralSecurityException {
    return pointDecode(curve, format, encoded);
  }

  /**
   * Decodes an encoded point on an elliptic curve. This method checks that the encoded point is on
   * the curve.
   *
   * @param curve the elliptic curve
   * @param format the format used to enocde the point
   * @param encoded the encoded point
   * @return the point
   * @throws GeneralSecurityException if the encoded point is invalid or if the curve or format are
   *     not supported.
   */
  public static ECPoint pointDecode(CurveType curveType, PointFormatType format, byte[] encoded)
      throws GeneralSecurityException {
    return pointDecode(getCurveSpec(curveType).getCurve(), format, encoded);
  }

  /**
   * Decodes an encoded point on an elliptic curve. This method checks that the encoded point is on
   * the curve.
   *
   * @param curve the elliptic curve
   * @param format the format used to enocde the point
   * @param encoded the encoded point
   * @return the point
   * @throws GeneralSecurityException if the encoded point is invalid or if the curve or format are
   *     not supported.
   */
  public static ECPoint pointDecode(EllipticCurve curve, PointFormatType format, byte[] encoded)
      throws GeneralSecurityException {
    int coordinateSize = fieldSizeInBytes(curve);
    switch (format) {
      case UNCOMPRESSED:
        {
          if (encoded.length != 2 * coordinateSize + 1) {
            throw new GeneralSecurityException("invalid point size");
          }
          if (encoded[0] != 4) {
            throw new GeneralSecurityException("invalid point format");
          }
          BigInteger x = new BigInteger(1, Arrays.copyOfRange(encoded, 1, coordinateSize + 1));
          BigInteger y =
              new BigInteger(1, Arrays.copyOfRange(encoded, coordinateSize + 1, encoded.length));
          ECPoint point = new ECPoint(x, y);
          checkPointOnCurve(point, curve);
          return point;
        }
      case COMPRESSED:
        {
          BigInteger p = getModulus(curve);
          if (encoded.length != coordinateSize + 1) {
            throw new GeneralSecurityException("compressed point has wrong length");
          }
          boolean lsb;
          if (encoded[0] == 2) {
            lsb = false;
          } else if (encoded[0] == 3) {
            lsb = true;
          } else {
            throw new GeneralSecurityException("invalid format");
          }
          BigInteger x = new BigInteger(1, Arrays.copyOfRange(encoded, 1, encoded.length));
          if (x.signum() == -1 || x.compareTo(p) != -1) {
            throw new GeneralSecurityException("x is out of range");
          }
          BigInteger y = getY(x, lsb, curve);
          return new ECPoint(x, y);
        }
      default:
        throw new GeneralSecurityException("invalid format:" + format);
    }
  }

  /**
   * Encodes a point on an elliptic curve.
   *
   * @param curve the elliptic curve
   * @param format the format for the encoding
   * @param point the point to encode
   * @return the encoded key exchange
   * @throws GeneralSecurityException if the point is not on the curve or if the format is not
   *     supported.
   */
  public static byte[] pointEncode(CurveType curveType, PointFormatType format, ECPoint point)
      throws GeneralSecurityException {
    return pointEncode(getCurveSpec(curveType).getCurve(), format, point);
  }

  /**
   * Encodes a point on an elliptic curve.
   *
   * @param curve the elliptic curve
   * @param format the format for the encoding
   * @param point the point to encode
   * @return the encoded key exchange
   * @throws GeneralSecurityException if the point is not on the curve or if the format is not
   *     supported.
   */
  public static byte[] pointEncode(EllipticCurve curve, PointFormatType format, ECPoint point)
      throws GeneralSecurityException {
    checkPointOnCurve(point, curve);
    int coordinateSize = fieldSizeInBytes(curve);
    switch (format) {
      case UNCOMPRESSED:
        {
          byte[] encoded = new byte[2 * coordinateSize + 1];
          byte[] x = point.getAffineX().toByteArray();
          byte[] y = point.getAffineY().toByteArray();
          // Order of System.arraycopy is important because x,y can have leading 0's.
          System.arraycopy(y, 0, encoded, 1 + 2 * coordinateSize - y.length, y.length);
          System.arraycopy(x, 0, encoded, 1 + coordinateSize - x.length, x.length);
          encoded[0] = 4;
          return encoded;
        }
      case COMPRESSED:
        {
          byte[] encoded = new byte[coordinateSize + 1];
          byte[] x = point.getAffineX().toByteArray();
          System.arraycopy(x, 0, encoded, 1 + coordinateSize - x.length, x.length);
          encoded[0] = (byte) (point.getAffineY().testBit(0) ? 3 : 2);
          return encoded;
        }
      default:
        throw new GeneralSecurityException("invalid format:" + format);
    }
  }

  /**
   * Returns the ECParameterSpec for a named curve.
   *
   * @param curve the curve type
   * @return the ECParameterSpec for the curve.
   */
  public static ECParameterSpec getCurveSpec(CurveType curve) throws NoSuchAlgorithmException {
    switch (curve) {
      case NIST_P256:
        return getNistP256Params();
      case NIST_P384:
        return getNistP384Params();
      case NIST_P521:
        return getNistP521Params();
      default:
        throw new NoSuchAlgorithmException("curve not implemented:" + curve);
    }
  }

  /**
   * Returns an {@link ECPublicKey} from {@code x509PublicKey} which is an encoding of a public
   * key, encoded according to the ASN.1 type SubjectPublicKeyInfo.
   *
   * TODO(b/68672497): test that in Java one can always get this representation by using
   * {@link ECPublicKey#getEncoded), regardless of the provider.
   */
  public static ECPublicKey getEcPublicKey(final byte[] x509PublicKey)
      throws GeneralSecurityException {
    KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
    return (ECPublicKey) kf.generatePublic(new X509EncodedKeySpec(x509PublicKey));
  }

  /**
   * Returns an {@link ECPublicKey} from {@code publicKey} that is a public key in point format
   * {@code pointFormat} on {@code curve}.
   */
  public static ECPublicKey getEcPublicKey(
      CurveType curve, PointFormatType pointFormat, final byte[] publicKey)
      throws GeneralSecurityException {
    return getEcPublicKey(getCurveSpec(curve), pointFormat, publicKey);
  }

  /**
   * Returns an {@link ECPublicKey} from {@code publicKey} that is a public key in point format
   * {@code pointFormat} on {@code curve}.
   */
  public static ECPublicKey getEcPublicKey(
      ECParameterSpec spec, PointFormatType pointFormat, final byte[] publicKey)
      throws GeneralSecurityException {
    ECPoint point = pointDecode(spec.getCurve(), pointFormat, publicKey);
    ECPublicKeySpec pubSpec = new ECPublicKeySpec(point, spec);
    KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
    return (ECPublicKey) kf.generatePublic(pubSpec);
  }

  /**
   * Returns an {@code ECPublicKey} from {@code curve} type and {@code x} and {@code y} coordinates.
   */
  public static ECPublicKey getEcPublicKey(CurveType curve, final byte[] x, final byte[] y)
      throws GeneralSecurityException {
    ECParameterSpec ecParams = getCurveSpec(curve);
    BigInteger pubX = new BigInteger(1, x);
    BigInteger pubY = new BigInteger(1, y);
    ECPoint w = new ECPoint(pubX, pubY);
    checkPointOnCurve(w, ecParams.getCurve());
    ECPublicKeySpec spec = new ECPublicKeySpec(w, ecParams);
    KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
    return (ECPublicKey) kf.generatePublic(spec);
  }

  /**
   * Returns an {@code ECPrivateKey} from {@code pkcs8PrivateKey} which is an encoding of a private
   * key, encoded according to the ASN.1 type SubjectPublicKeyInfo.
   *
   * TODO(b/68672497): test that in Java one can always get this representation by using
   * {@link ECPrivateKey#getEncoded), regardless of the provider.
   */
  public static ECPrivateKey getEcPrivateKey(final byte[] pkcs8PrivateKey)
      throws GeneralSecurityException {
    KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
    return (ECPrivateKey) kf.generatePrivate(new PKCS8EncodedKeySpec(pkcs8PrivateKey));
  }

  /** Returns an {@code ECPrivateKey} from {@code curve} type and {@code keyValue}. */
  public static ECPrivateKey getEcPrivateKey(CurveType curve, final byte[] keyValue)
      throws GeneralSecurityException {
    ECParameterSpec ecParams = getCurveSpec(curve);
    BigInteger privValue = new BigInteger(1, keyValue);
    ECPrivateKeySpec spec = new ECPrivateKeySpec(privValue, ecParams);
    KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
    return (ECPrivateKey) kf.generatePrivate(spec);
  }

  /** Generates a new key pair for {@code curve}. */
  public static KeyPair generateKeyPair(CurveType curve) throws GeneralSecurityException {
    return generateKeyPair(getCurveSpec(curve));
  }

  /** Generates a new key pair for {@code spec}. */
  public static KeyPair generateKeyPair(ECParameterSpec spec) throws GeneralSecurityException {
    KeyPairGenerator keyGen = EngineFactory.KEY_PAIR_GENERATOR.getInstance("EC");
    keyGen.initialize(spec);
    return keyGen.generateKeyPair();
  }

  /**
   * Checks that the shared secret is on the curve of the private key, to prevent arithmetic errors
   * or fault attacks.
   */
  private static void validateSharedSecret(byte[] secret, ECPrivateKey privateKey)
      throws GeneralSecurityException {
    EllipticCurve privateKeyCurve = privateKey.getParams().getCurve();
    BigInteger x = new BigInteger(1, secret);
    if (x.signum() == -1 || x.compareTo(getModulus(privateKeyCurve)) != -1) {
      throw new GeneralSecurityException("shared secret is out of range");
    }
    // This will throw if x is not a valid coordinate.
    getY(x, true /* lsb, doesn't matter here */, privateKeyCurve);
  }

  /* Generates the DH shared secret using {@code myPrivateKey} and {@code peerPublicKey} */
  public static byte[] computeSharedSecret(ECPrivateKey myPrivateKey, ECPublicKey peerPublicKey)
      throws GeneralSecurityException {
    validatePublicKeySpec(peerPublicKey, myPrivateKey);
    return computeSharedSecret(myPrivateKey, peerPublicKey.getW());
  }

  /* Generates the DH shared secret using {@code myPrivateKey} and {@code publicPoint} */
  public static byte[] computeSharedSecret(ECPrivateKey myPrivateKey, ECPoint publicPoint)
      throws GeneralSecurityException {
    checkPointOnCurve(publicPoint, myPrivateKey.getParams().getCurve());
    // Explicitly reconstruct the peer public key using private key's spec.
    ECParameterSpec privSpec = myPrivateKey.getParams();
    EllipticCurve privCurve = privSpec.getCurve();
    ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(publicPoint, privSpec);
    KeyFactory kf = KeyFactory.getInstance("EC");
    PublicKey publicKey = kf.generatePublic(publicKeySpec);
    KeyAgreement ka = EngineFactory.KEY_AGREEMENT.getInstance("ECDH");
    ka.init(myPrivateKey);
    ka.doPhase(publicKey, true /* lastPhase */);
    byte[] secret = ka.generateSecret();
    validateSharedSecret(secret, myPrivateKey);
    return secret;
  }
}
