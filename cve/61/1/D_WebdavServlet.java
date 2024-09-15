@@ -243,6 +243,7 @@ public class WebdavServlet
         try {
             documentBuilderFactory = DocumentBuilderFactory.newInstance();
             documentBuilderFactory.setNamespaceAware(true);
+            documentBuilderFactory.setExpandEntityReferences(false);
             documentBuilder = documentBuilderFactory.newDocumentBuilder();
         } catch(ParserConfigurationException e) {
             throw new ServletException
