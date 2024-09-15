@@ -80,7 +80,7 @@ public class ClassicConfiguration implements Configuration {
         getModernFlyway().setDefaultSchema(defaultSchema);
     }
 
-    public String getDefaultSchema(){
+    public String getDefaultSchema() {
         return getModernFlyway().getDefaultSchema();
     }
 

 
@@ -225,8 +228,8 @@ public class ClassicConfiguration implements Configuration {
             return new ValidatePattern[0];
         } else {
             return Arrays.stream(ignoreMigrationPatterns)
-                                                 .map(ValidatePattern::fromPattern)
-                                                 .toArray(ValidatePattern[]::new);
+                         .map(ValidatePattern::fromPattern)
+                         .toArray(ValidatePattern[]::new);
         }
     }




@@ -1191,7 +1178,7 @@ public class ClassicConfiguration implements Configuration {
             Class<? extends Callback> callbackClass;
             try {
                 callbackClass = ClassUtils.loadClass(Callback.class, callback, classLoader);
-            } catch(Throwable e) {
+            } catch (Throwable e) {
                 Throwable rootCause = ExceptionUtils.getRootCause(e);
                 LOG.warn("Skipping " + Callback.class + ": " + ClassUtils.formatThrowable(e) + (
                         rootCause == e
@@ -1216,7 +1203,7 @@ public class ClassicConfiguration implements Configuration {
      * @param resolvers The custom MigrationResolvers to be used in addition to the built-in ones for resolving Migrations to apply. (default: empty list)
      */
     public void setResolvers(MigrationResolver... resolvers) {
-        getModernFlyway().setMigrationResolvers(Arrays.stream(resolvers).map(Object::toString).collect(Collectors.toList()));
+        this.resolvers = resolvers;
     }
 

@@ -1358,18 +1345,29 @@ public class ClassicConfiguration implements Configuration {
      
     public void configure(Configuration configuration) {
         setModernConfig(ConfigurationModel.clone(configuration.getModernConfig()));
-        
+
         setJavaMigrations(configuration.getJavaMigrations());
         setResourceProvider(configuration.getResourceProvider());
         setJavaMigrationClassProvider(configuration.getJavaMigrationClassProvider());
         setCallbacks(configuration.getCallbacks());
 
-        this.dataSource = configuration.getDataSource();
+        List<MigrationResolver> migrationResolvers = new ArrayList<>(Arrays.asList(configuration.getResolvers()));
+        if (getModernFlyway().getMigrationResolvers() != null) {
+            String[] resolversFromConfig = getModernFlyway().getMigrationResolvers().toArray(new String[0]);
+            List<MigrationResolver> resolverList = ClassUtils.instantiateAll(resolversFromConfig, classLoader);
+            migrationResolvers.addAll(resolverList);
+        }
 
+        setResolvers(migrationResolvers.toArray(new MigrationResolver[0]));
 
+        getModernFlyway().setMigrationResolvers(null);
 
+        dataSource = configuration.getDataSource();
         pluginRegister = configuration.getPluginRegister();
 
+
+
+
         configureFromConfigurationProviders(this);
     }
 
@@ -1379,17 +1377,17 @@ public class ClassicConfiguration implements Configuration {

 
-    public void setUrl(String url){
+    public void setUrl(String url) {
         this.dataSource = null;
         getCurrentUnresolvedEnvironment().setUrl(url);
         this.resolvedEnvironments.clear();
-
     }
 
     public void setUser(String user) {

@@ -1670,7 +1669,7 @@ public class ClassicConfiguration implements Configuration {
             setFailOnMissingLocations(failOnMissingLocationsProp);
         }
 
-        if(StringUtils.hasText(getCurrentEnvironment().getUrl()) && (dataSource == null || StringUtils.hasText(urlProp) || StringUtils.hasText(driverProp) || StringUtils.hasText(userProp) || StringUtils.hasText(passwordProp))) {
+        if (StringUtils.hasText(getCurrentEnvironment().getUrl()) && (dataSource == null || StringUtils.hasText(urlProp) || StringUtils.hasText(driverProp) || StringUtils.hasText(userProp) || StringUtils.hasText(passwordProp))) {
             Map<String, String> jdbcPropertiesFromProps = getPropertiesUnderNamespace(props, getPlaceholders(), ConfigUtils.JDBC_PROPERTIES_PREFIX);
             setDataSource(new DriverDataSource(classLoader, getCurrentEnvironment().getDriver(), getCurrentEnvironment().getUrl(), getCurrentEnvironment().getUser(), getCurrentEnvironment().getPassword(), this, jdbcPropertiesFromProps));
         }
