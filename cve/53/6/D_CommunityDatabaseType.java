@@ -16,10 +16,11 @@

 
-public abstract class CommunityDatabaseType extends BaseDatabaseType{
+public interface CommunityDatabaseType extends DatabaseType {
 
-    final public String announcementForCommunitySupport() {
+     default String announcementForCommunitySupport() {
         return getName() + " is a community contributed database, see "+ COMMUNITY_CONTRIBUTED_DATABASES + " for more details";
     }
 
