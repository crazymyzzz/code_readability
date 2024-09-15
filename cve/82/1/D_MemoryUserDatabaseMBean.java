@@ -173,7 +173,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             MBeanUtils.createMBean(group);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception creating group " + group + " MBean");
+                ("Exception creating group [" + groupname + "] MBean");
             iae.initCause(e);
             throw iae;
         }
@@ -196,7 +196,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             MBeanUtils.createMBean(role);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception creating role " + role + " MBean");
+                ("Exception creating role [" + rolename + "] MBean");
             iae.initCause(e);
             throw iae;
         }
@@ -221,7 +221,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             MBeanUtils.createMBean(user);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception creating user " + user + " MBean");
+                ("Exception creating user [" + username + "] MBean");
             iae.initCause(e);
             throw iae;
         }
@@ -249,7 +249,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             return (oname.toString());
         } catch (MalformedObjectNameException e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Cannot create object name for group " + group);
+                ("Cannot create object name for group [" + groupname + "]");
             iae.initCause(e);
             throw iae;
         }
@@ -276,7 +276,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             return (oname.toString());
         } catch (MalformedObjectNameException e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Cannot create object name for role " + role);
+                ("Cannot create object name for role [" + rolename + "]");
             iae.initCause(e);
             throw iae;
         }
@@ -303,7 +303,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             return (oname.toString());
         } catch (MalformedObjectNameException e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Cannot create object name for user " + user);
+                ("Cannot create object name for user [" + username + "]");
             iae.initCause(e);
             throw iae;
         }
@@ -328,7 +328,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             database.removeGroup(group);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception destroying group " + group + " MBean");
+                ("Exception destroying group [" + groupname + "] MBean");
             iae.initCause(e);
             throw iae;
         }
@@ -353,7 +353,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             database.removeRole(role);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception destroying role " + role + " MBean");
+                ("Exception destroying role [" + rolename + "] MBean");
             iae.initCause(e);
             throw iae;
         }
@@ -378,7 +378,7 @@ public class MemoryUserDatabaseMBean extends BaseModelMBean {
             database.removeUser(user);
         } catch (Exception e) {
             IllegalArgumentException iae = new IllegalArgumentException
-                ("Exception destroying user " + user + " MBean");
+                ("Exception destroying user [" + username + "] MBean");
             iae.initCause(e);
             throw iae;
         }
