@@ -1672,8 +1672,10 @@ public class ClassicConfiguration implements Configuration {
         }
 
         if (StringUtils.hasText(getCurrentResolvedEnvironment().getUrl()) && (dataSource == null || StringUtils.hasText(urlProp) || StringUtils.hasText(driverProp) || StringUtils.hasText(userProp) || StringUtils.hasText(passwordProp))) {
+            Map<String, String> jdbcProperties = Optional.ofNullable(getCurrentResolvedEnvironment().getJdbcProperties()).orElse(new HashMap<>());
             Map<String, String> jdbcPropertiesFromProps = getPropertiesUnderNamespace(props, getPlaceholders(), ConfigUtils.JDBC_PROPERTIES_PREFIX);
-            setDataSource(new DriverDataSource(classLoader, getCurrentResolvedEnvironment().getDriver(), getCurrentResolvedEnvironment().getUrl(), getCurrentResolvedEnvironment().getUser(), getCurrentResolvedEnvironment().getPassword(), this, jdbcPropertiesFromProps));
+            jdbcProperties.putAll(jdbcPropertiesFromProps);
+            setDataSource(new DriverDataSource(classLoader, getCurrentResolvedEnvironment().getDriver(), getCurrentResolvedEnvironment().getUrl(), getCurrentResolvedEnvironment().getUser(), getCurrentResolvedEnvironment().getPassword(), this, jdbcProperties));
         }
 
         ConfigUtils.checkConfigurationForUnrecognisedProperties(props, "flyway.");
