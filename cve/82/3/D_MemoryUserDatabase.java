@@ -585,7 +585,7 @@ public class MemoryUserDatabase implements UserDatabase {
             values = getUsers();
             while (values.hasNext()) {
                 writer.print("  ");
-                writer.println(values.next());
+                writer.println(((MemoryUser) values.next()).toXml());
             }
 
             // Print the file epilog
