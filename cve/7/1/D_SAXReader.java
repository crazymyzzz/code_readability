@@ -98,6 +98,8 @@ public final class SAXReader implements ContentHandler, EntityResolver, DTDHandl
         // Get reader and parse using SAX2 API
         try {
             XMLReader saxReader = parser.getSAXParser().getXMLReader();
+            saxReader.setFeature(javax.xml.XMLConstants.FEATURE_SECURE_PROCESSING, true );
+
             saxReader.setErrorHandler(new ParseErrorHandler());
             saxReader.setContentHandler(this);
             saxReader.setEntityResolver(this);
