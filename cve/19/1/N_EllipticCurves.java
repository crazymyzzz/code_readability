

/** Utility functions and enums for elliptic curve crypto, used in ECDSA and ECDH. */
public final class EllipticCurves {

  

  /** Checks that the public key's params spec is the same as the private key's params spec. */
  static void validatePublicKeySpec(ECPublicKey publicKey, ECPrivateKey privateKey)
      throws GeneralSecurityException {
    try {
      ECParameterSpec publicKeySpec = publicKey.getParams();
      ECParameterSpec privateKeySpec = privateKey.getParams();
      if (!publicKeySpec.getCurve().equals(privateKeySpec.getCurve())
          || !publicKeySpec.getGenerator().equals(privateKeySpec.getGenerator())
          || !publicKeySpec.getOrder().equals(privateKeySpec.getOrder())
          || publicKeySpec.getCofactor() != privateKeySpec.getCofactor()) {
        throw new GeneralSecurityException("invalid public key spec");
      }
    } catch (IllegalArgumentException | NullPointerException ex) {
      // The Java security providers on Android K and Android L might throw these unchecked
      // exceptions, converting them to a checked one to not crash the JVM.
      throw new GeneralSecurityException(ex.toString());
    }
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
    try {
      ka.doPhase(publicKey, true /* lastPhase */);
      byte[] secret = ka.generateSecret();
      validateSharedSecret(secret, myPrivateKey);
      return secret;
    } catch (IllegalStateException ex) {
      // Due to CVE-2017-10176 some versions of OpenJDK might throw this unchecked exception,
      // converting it to a checked one to not crash the JVM. See also b/73760761.
      throw new GeneralSecurityException(ex.toString());
    }
  }
}
