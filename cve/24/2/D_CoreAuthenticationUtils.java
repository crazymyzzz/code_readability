@@ -113,6 +113,7 @@ public class CoreAuthenticationUtils {
             case "multivalued":
             case "multi_valued":
             case "combine":
+            case "merge":
                 return new MultivaluedAttributeMerger();
             case "add":
                 return new NoncollidingAttributeAdder();
@@ -120,7 +121,7 @@ public class CoreAuthenticationUtils {
             case "overwrite":
             case "override":
                 return new ReplacingAttributeAdder();
-            default:
+            case "none":
                 return new BaseAdditiveAttributeMerger() {
                     @Override
                     protected Map<String, List<Object>> mergePersonAttributes(final Map<String, List<Object>> toModify,
@@ -128,6 +129,8 @@ public class CoreAuthenticationUtils {
                         return new LinkedHashMap<>(toModify);
                     }
                 };
+            default:
+                throw new IllegalArgumentException("Unsupported merging policy [" + mergingPolicy + "]");
         }
     }
 
