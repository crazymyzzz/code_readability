@@ -90,24 +90,28 @@ public final class TldConfig  implements LifecycleListener {
             if (tldDigesters[0] == null) {
                 tldDigesters[0] = DigesterFactory.newDigester(validation,
                         namespaceAware, new TldRuleSet());
+                tldDigesters[0].getParser();
             }
             digester = tldDigesters[0];
         } else if (!namespaceAware && validation) {
             if (tldDigesters[1] == null) {
                 tldDigesters[1] = DigesterFactory.newDigester(validation,
                         namespaceAware, new TldRuleSet());
+                tldDigesters[1].getParser();
             }
             digester = tldDigesters[1];
         } else if (namespaceAware && !validation) {
             if (tldDigesters[2] == null) {
                 tldDigesters[2] = DigesterFactory.newDigester(validation,
                         namespaceAware, new TldRuleSet());
+                tldDigesters[2].getParser();
             }
             digester = tldDigesters[2];
         } else {
             if (tldDigesters[3] == null) {
                 tldDigesters[3] = DigesterFactory.newDigester(validation,
                         namespaceAware, new TldRuleSet());
+                tldDigesters[3].getParser();
             }
             digester = tldDigesters[3];
         }
