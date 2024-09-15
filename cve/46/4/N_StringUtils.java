

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {


    public static Pair<String,String> getFileNameAndExtension(String path) {
        String[] foldersSplit = path.split("[|/\\\\]");
        String fileNameAndExtension = foldersSplit[foldersSplit.length - 1];

        String[] nameExtensionSplit = fileNameAndExtension.split("\\.");
        if (nameExtensionSplit.length < 2) {
            return Pair.of(fileNameAndExtension, "");
        }

        return Pair.of(nameExtensionSplit[nameExtensionSplit.length - 2], nameExtensionSplit[nameExtensionSplit.length - 1]);
    }

}