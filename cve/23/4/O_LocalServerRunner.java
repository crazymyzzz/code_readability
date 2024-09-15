

public class LocalServerRunner {

    

    private synchronized void installAndStartServer(String repositoryURL, int localServerPort)
            throws IOException {
        if (serverProcess != null) {
            return;
        }

        String configFile =
                LocalServerRunner.class.getResource("/test-server.properties").getFile();
        String tempDir = System.getProperty("java.io.tmpdir");
        Path serverFile = Paths.get(tempDir, "conductor-server.jar");
        if (!Files.exists(serverFile)) {
            Files.copy(new URL(repositoryURL).openStream(), serverFile);
        }

        String command =
                "java -Dserver.port="
                        + localServerPort
                        + " -DCONDUCTOR_CONFIG_FILE="
                        + configFile
                        + " -jar "
                        + serverFile;
        LOGGER.info("Running command {}", command);

        serverProcess = Runtime.getRuntime().exec(command);
        BufferedReader error =
                new BufferedReader(new InputStreamReader(serverProcess.getErrorStream()));
        BufferedReader op =
                new BufferedReader(new InputStreamReader(serverProcess.getInputStream()));

        // This captures the stream and copies to a visible log for tracking errors asynchronously
        // using a separate thread
        Executors.newSingleThreadScheduledExecutor()
                .execute(
                        () -> {
                            String line = null;
                            while (true) {
                                try {
                                    if ((line = error.readLine()) == null) break;
                                } catch (IOException e) {
                                    LOGGER.error("Exception reading input stream:", e);
                                }
                                // copy to standard error
                                LOGGER.error("Server error stream - {}", line);
                            }
                        });

        // This captures the stream and copies to a visible log for tracking errors asynchronously
        // using a separate thread
        Executors.newSingleThreadScheduledExecutor()
                .execute(
                        () -> {
                            String line = null;
                            while (true) {
                                try {
                                    if ((line = op.readLine()) == null) break;
                                } catch (IOException e) {
                                    LOGGER.error("Exception reading input stream:", e);
                                }
                                // copy to standard out
                                LOGGER.trace("Server input stream - {}", line);
                            }
                        });
    }
}
