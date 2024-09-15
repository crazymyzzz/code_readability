

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class StringUtils {


    public static String getFileExtension(String path) {
        String[] foldersSplit = path.split("[|/]");
        String fileNameAndExtension = foldersSplit[foldersSplit.length - 1];

        String[] nameExtensionSplit = fileNameAndExtension.split("\\.");
        if (nameExtensionSplit.length < 2) {
            return "";
        }

        return nameExtensionSplit[nameExtensionSplit.length - 1];
    }
}