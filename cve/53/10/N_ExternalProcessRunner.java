
public class ExternalProcessRunner {

    @SneakyThrows
    public int run(final String[] command, final Consumer<? super String> onStdOut,
        final Consumer<? super String> onStdErr) {
        return run(command, null, onStdOut, onStdErr);
    }

    @SneakyThrows
    public int run(final String[] command, final Path workingDirectory,
        final Consumer<? super String> onStdOut, final Consumer<? super String> onStdErr) {
        final ProcessBuilder processBuilder = new ProcessBuilder(command);
        if (workingDirectory != null) {
            processBuilder.directory(workingDirectory.toFile());
        }
        final Process process = processBuilder.start();

        final ExecutorService executorService = Executors.newFixedThreadPool(2);
        try (final InputStreamReader outStream = new InputStreamReader(process.getInputStream(),
            StandardCharsets.UTF_8);
            final InputStreamReader errStream = new InputStreamReader(process.getErrorStream(), StandardCharsets.UTF_8);
            final Scanner stdOut = new Scanner(outStream);
            final Scanner stdErr = new Scanner(errStream)
        ) {
            final Future<?> handleStdOut = executorService.submit(() -> {
                while (stdOut.hasNextLine()) {
                    onStdOut.accept(stdOut.nextLine());
                }
            });
            final Future<?> handleStdErr = executorService.submit(() -> {
                while (stdErr.hasNextLine()) {
                    onStdErr.accept(stdErr.nextLine());
                }
            });
            handleStdOut.get();
            handleStdErr.get();
        } finally {
            executorService.shutdown();
        }

        return process.waitFor();
    }
}