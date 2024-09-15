@@ -99,6 +99,7 @@ public final class SAXReader implements ContentHandler, EntityResolver, DTDHandl
         try {
             XMLReader saxReader = parser.getSAXParser().getXMLReader();
             saxReader.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true );
+            saxReader.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
 
             saxReader.setErrorHandler(new ParseErrorHandler());
             saxReader.setContentHandler(this);
