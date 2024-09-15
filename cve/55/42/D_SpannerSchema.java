@@ -149,7 +149,7 @@ public class SpannerSchema extends Schema<SpannerDatabase, SpannerTable> {
                                                                      "AND TABLE_SCHEMA=''");
 
         for (Result result : foreignKeyRs.getResults()) {
-            for (List<String> row : result.getData()) {
+            for (List<String> row : result.data()) {
                 String[] foreignKeyAndTable = {row.get(0), row.get(1)};
                 foreignKeyAndTableList.add(foreignKeyAndTable);
             }
