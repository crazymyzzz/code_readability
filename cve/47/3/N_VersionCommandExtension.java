

@CustomLog
public class VersionCommandExtension implements CommandExtension {


    @Override
    public OperationResult handle(String command, Configuration config, List<String> flags) throws FlywayException {
        VersionPrinter.printVersionOnly();
        LOG.info("");

        LOG.debug("Java " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        LOG.debug(System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + "\n");

        List<Plugin> allPlugins = config.getPluginRegister().getPlugins(Plugin.class);

        List<PluginVersionResult> pluginVersions = allPlugins.stream()
                                                             .map(p -> new PluginVersionResult(p.getClass().getSimpleName(), p.getPluginVersion(), p.isLicensed(config)))
                                                             .filter(p -> StringUtils.hasText(p.version))
                                                             .collect(Collectors.toList());

        int nameLength = pluginVersions.stream().map(p -> p.name.length()).max(Integer::compare).get() + 2;
        int versionLength = pluginVersions.stream().map(p -> p.version.length()).max(Integer::compare).get() + 2;

        LOG.info(StringUtils.rightPad("Plugin Name", nameLength, ' ') + " | " +
                                      StringUtils.rightPad("Version", versionLength, ' ') + " | " +
                                      ("Licensed"));

        LOG.info(StringUtils.rightPad(StringUtils.leftPad("", nameLength, '-'), nameLength, ' ') + " | " +
                                      StringUtils.rightPad(StringUtils.leftPad("", versionLength, '-'), versionLength, ' ') + " | " +
                                      ("--------"));
        for (PluginVersionResult p : pluginVersions) {
            LOG.info(StringUtils.rightPad(p.name, nameLength, ' ') + " | " +
                                          StringUtils.rightPad(p.version, versionLength, ' ') + " | " +
                                          (p.isLicensed ? "Licensed" : "Unlicensed"));
        }

        return new VersionResult(VersionPrinter.getVersion(), command, VersionPrinter.EDITION, pluginVersions);
    }


}