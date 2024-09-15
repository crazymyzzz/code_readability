@@ -76,7 +76,7 @@ import static org.junit.jupiter.api.Assertions.*;
 public class AmazonS3SamlIdPMetadataGeneratorTests {
     static {
         System.setProperty(SkipMd5CheckStrategy.DISABLE_GET_OBJECT_MD5_VALIDATION_PROPERTY, "true");
-        System.setProperty("com.amazonaws.services.s3.disableGetObjectMD5Validation", "true");
+        System.setProperty(SkipMd5CheckStrategy.DISABLE_PUT_OBJECT_MD5_VALIDATION_PROPERTY, "true");
     }
 
     @Autowired
