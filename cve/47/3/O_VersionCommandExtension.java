

@CustomLog
public class VersionCommandExtension implements CommandExtension {


    @Override
    public OperationResult handle(String command, Configuration config, List<String> flags) throws FlywayException {
        VersionPrinter.printVersionOnly();
        LOG.info("");

        LOG.debug("Java " + System.getProperty("java.version") + " (" + System.getProperty("java.vendor") + ")");
        LOG.debug(System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch") + "\n");

        return new VersionResult(VersionPrinter.getVersion(), command, VersionPrinter.EDITION);
    }


}