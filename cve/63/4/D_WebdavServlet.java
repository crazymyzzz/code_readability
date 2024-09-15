
@@ -1628,7 +1566,7 @@ public class WebdavServlet
         }
 
         // Normalise destination path (remove '.' and '..')
-        destinationPath = normalize(destinationPath);
+        destinationPath = RequestUtil.normalize(destinationPath);
 
         String contextPath = req.getContextPath();
         if ((contextPath != null) &&
@@ -2383,7 +2321,8 @@ public class WebdavServlet
         if (!toAppend.startsWith("/"))
             toAppend = "/" + toAppend;
 
-        generatedXML.writeText(rewriteUrl(normalize(absoluteUri + toAppend)));
+        generatedXML.writeText(rewriteUrl(RequestUtil.normalize(
+                absoluteUri + toAppend)));
 
         generatedXML.writeElement(null, "href", XMLWriter.CLOSING);
 
