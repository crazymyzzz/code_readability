

/**
 * A {@link FileDestination} that writes a unix-patch file to {@code rootPath} containing the
 * suggested changes.
 */
public final class PatchFileDestination implements FileDestination {



  @Override
  public void writeFile(SourceFile update) throws IOException {
    Path sourceFilePath = rootPath.resolve(update.getPath());
    String oldSource = new String(Files.readAllBytes(sourceFilePath), UTF_8);
    String newSource = update.getSourceText();
    if (!oldSource.equals(newSource)) {
      List<String> originalLines = LINE_SPLITTER.splitToList(oldSource);

      Patch<String> diff = null;
      try {
        diff = DiffUtils.diff(originalLines, LINE_SPLITTER.splitToList(newSource));
      } catch (DiffException e) {
        throw new AssertionError("DiffUtils.diff should not fail", e);
      }
      String relativePath = baseDir.relativize(sourceFilePath).toString();
      List<String> unifiedDiff =
          UnifiedDiffUtils.generateUnifiedDiff(relativePath, relativePath, originalLines, diff, 2);
      String diffString = Joiner.on("\n").join(unifiedDiff) + "\n";
      diffByFile.put(sourceFilePath.toUri(), diffString);
    }
  }


}
