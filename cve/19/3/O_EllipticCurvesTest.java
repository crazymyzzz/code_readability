

/** Unit tests for {@link com.google.crypto.tink.subtle.EllipticCurves}. */
@RunWith(JUnit4.class)
public class EllipticCurvesTest {
  

  @Test
  public void testComputeSharedSecretWithWycheproofTestVectors() throws Exception {
    JSONObject json =
        WycheproofTestUtil.readJson("testdata/wycheproof/ecdh_test.json");
    int errors = 0;
    JSONArray testGroups = json.getJSONArray("testGroups");
    for (int i = 0; i < testGroups.length(); i++) {
      JSONObject group = testGroups.getJSONObject(i);
      JSONArray tests = group.getJSONArray("tests");
      for (int j = 0; j < tests.length(); j++) {
        JSONObject testcase = tests.getJSONObject(j);
        String tcId =
            String.format(
                "testcase %d (%s)", testcase.getInt("tcId"), testcase.getString("comment"));
        String result = testcase.getString("result");
        String hexPubKey = testcase.getString("public");
        String expectedSharedSecret = testcase.getString("shared");
        String curve = testcase.getString("curve");
        String hexPrivKey = testcase.getString("private");
        if (hexPrivKey.length() % 2 == 1) {
          hexPrivKey = "0" + hexPrivKey;
        }
        KeyFactory kf = EngineFactory.KEY_FACTORY.getInstance("EC");
        try {
          EllipticCurves.CurveType curveType = WycheproofTestUtil.getCurveType(curve);
          ECPrivateKey privKey = EllipticCurves.getEcPrivateKey(curveType, Hex.decode(hexPrivKey));
          X509EncodedKeySpec x509keySpec = new X509EncodedKeySpec(Hex.decode(hexPubKey));
          ECPublicKey pubKey = (ECPublicKey) kf.generatePublic(x509keySpec);
          String sharedSecret = Hex.encode(EllipticCurves.computeSharedSecret(privKey, pubKey));
          if (result.equals("invalid")) {
            System.out.printf(
                "FAIL %s: accepting invalid parameters, shared secret: %s%n", tcId, sharedSecret);
            errors++;
          } else if (!expectedSharedSecret.equals(sharedSecret)) {
            System.out.printf(
                "FAIL %s: incorrect shared secret, computed: %s, expected: %s%n",
                tcId, sharedSecret, expectedSharedSecret);
            errors++;
          }
        } catch (NoSuchAlgorithmException ex) {
          if (result.equals("valid")) {
            // When the curve is not implemented, this is the expected exception.
            continue;
          }
        } catch (GeneralSecurityException ex) {
          if (result.equals("valid")) {
            System.out.printf("FAIL %s, exception %s%n", tcId, ex);
            errors++;
          }
        } catch (Exception ex) {
          // Other exceptions typically indicate that something is wrong with the implementation.
          System.out.printf("FAIL %s, exception %s%n", tcId, ex);
          errors++;
        }
      }
    }
    assertEquals(0, errors);
  }
}
