@@ -17,13 +17,13 @@
 
 package org.jkiss.utils.xml;
 
-import javax.xml.XMLConstants;
 import org.jkiss.code.NotNull;
 import org.jkiss.code.Nullable;
 import org.w3c.dom.Document;
 import org.w3c.dom.Element;
 import org.xml.sax.InputSource;
 
+import javax.xml.XMLConstants;
 import javax.xml.parsers.DocumentBuilder;
 import javax.xml.parsers.DocumentBuilderFactory;
 import java.io.FileInputStream;
@@ -63,6 +63,7 @@ public class XMLUtils {
         try {
             DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
             dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
+            dbf.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
             DocumentBuilder xmlBuilder = dbf.newDocumentBuilder();
             return xmlBuilder.parse(source);
         } catch (Exception er) {
