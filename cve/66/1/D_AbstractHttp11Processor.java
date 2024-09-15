
@@ -764,7 +770,8 @@ public abstract class AbstractHttp11Processor {
         // Create and add the chunked filters.
         //getInputBuffer().addFilter(new GzipInputFilter());
         getOutputBuffer().addFilter(new GzipOutputFilter());
-
+        
+        pluggableFilterIndex = getInputBuffer().getFilters().length;
     }
 
     
@@ -807,7 +814,7 @@ public abstract class AbstractHttp11Processor {
                 (inputFilters[Constants.CHUNKED_FILTER]);
             contentDelimitation = true;
         } else {
-            for (int i = 2; i < inputFilters.length; i++) {
+            for (int i = pluggableFilterIndex; i < inputFilters.length; i++) {
                 if (inputFilters[i].getEncodingName()
                     .toString().equals(encodingName)) {
                     getInputBuffer().addActiveFilter(inputFilters[i]);
