@@ -484,6 +484,8 @@ public class ContextConfig
                         namespaceAware, webRuleSet);
                 webFragmentDigesters[0] = DigesterFactory.newDigester(validation,
                         namespaceAware, webFragmentRuleSet);
+                webDigesters[0].getParser();
+                webFragmentDigesters[0].getParser();
             }
             webDigester = webDigesters[0];
             webFragmentDigester = webFragmentDigesters[0];
@@ -494,6 +496,8 @@ public class ContextConfig
                         namespaceAware, webRuleSet);
                 webFragmentDigesters[1] = DigesterFactory.newDigester(validation,
                         namespaceAware, webFragmentRuleSet);
+                webDigesters[1].getParser();
+                webFragmentDigesters[1].getParser();
             }
             webDigester = webDigesters[1];
             webFragmentDigester = webFragmentDigesters[1];
@@ -504,6 +508,8 @@ public class ContextConfig
                         namespaceAware, webRuleSet);
                 webFragmentDigesters[2] = DigesterFactory.newDigester(validation,
                         namespaceAware, webFragmentRuleSet);
+                webDigesters[2].getParser();
+                webFragmentDigesters[2].getParser();
             }
             webDigester = webDigesters[2];
             webFragmentDigester = webFragmentDigesters[2];
@@ -514,6 +520,8 @@ public class ContextConfig
                         namespaceAware, webRuleSet);
                 webFragmentDigesters[3] = DigesterFactory.newDigester(validation,
                         namespaceAware, webFragmentRuleSet);
+                webDigesters[3].getParser();
+                webFragmentDigesters[3].getParser();
             }
             webDigester = webDigesters[3];
             webFragmentDigester = webFragmentDigesters[3];
@@ -842,6 +850,9 @@ public class ContextConfig
         
         contextConfig();
         
+        createWebXmlDigester(context.getXmlNamespaceAware(),
+                context.getXmlValidation());
+
         try {
             fixDocBase();
         } catch (IOException e) {
@@ -878,8 +889,6 @@ public class ContextConfig
                     Boolean.valueOf(context.getXmlNamespaceAware())));
         }
         
-        createWebXmlDigester(context.getXmlNamespaceAware(), context.getXmlValidation());
-        
         webConfig();
 
         if (!context.getIgnoreAnnotations()) {
