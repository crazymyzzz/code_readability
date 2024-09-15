@@ -28,6 +28,7 @@ import org.jkiss.dbeaver.runtime.DBWorkbench;
 import org.jkiss.utils.CommonUtils;
 import org.xml.sax.SAXException;
 
+import javax.xml.XMLConstants;
 import javax.xml.parsers.ParserConfigurationException;
 import javax.xml.parsers.SAXParser;
 import javax.xml.parsers.SAXParserFactory;
@@ -88,8 +89,11 @@ public class ShowTipOfTheDayHandler extends AbstractHandler {
             try (InputStream tipsInputStream = url.openConnection().getInputStream()) {
 
                 SAXParserFactory factory = SAXParserFactory.newInstance();
+                factory.setFeature( XMLConstants.FEATURE_SECURE_PROCESSING, true );
+
                 SAXParser saxParser = factory.newSAXParser();
 
+
                 TipsXmlHandler handler = new TipsXmlHandler();
                 saxParser.parse(tipsInputStream, handler);
                 result.addAll(handler.getTips());
