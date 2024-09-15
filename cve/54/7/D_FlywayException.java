@@ -23,7 +23,7 @@ import lombok.Getter;
 public class FlywayException extends RuntimeException {
 
     @Getter
-    private ErrorCode errorCode = ErrorCode.ERROR;
+    private ErrorCode errorCode = CoreErrorCode.ERROR;
 
     /**
      * Creates a new FlywayException with this message, cause, and error code.
