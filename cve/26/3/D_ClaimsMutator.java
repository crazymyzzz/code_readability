@@ -25,7 +25,7 @@ import java.util.Date;
  * @see io.jsonwebtoken.Claims
  * @since 0.2
  */
-public interface ClaimsMutator<T extends ClaimsMutator> {
+public interface ClaimsMutator<T extends ClaimsMutator<T>> {
 
     /**
      * Sets the JWT <a href="https://tools.ietf.org/html/draft-ietf-oauth-json-web-token-25#section-4.1.1">
