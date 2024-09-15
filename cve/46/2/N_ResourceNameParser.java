

public class ResourceNameParser {


    public ResourceName parse(String resourceName, String[] suffixes) {
        // Strip off suffixes
        Pair<String, String> suffixResult = stripSuffix(resourceName, suffixes);

        // Find the appropriate prefix
        Pair<String, ResourceType> prefix = findPrefix(suffixResult.getLeft(), prefixes);
        if (prefix != null) {

            // Strip off prefix
            Pair<String, String> prefixResult = stripPrefix(suffixResult.getLeft(), prefix.getLeft());
            String name = prefixResult.getRight();
            Pair<String, String> splitName = StringUtils.splitAtFirstSeparator(name, configuration.getSqlMigrationSeparator());
            boolean isValid = true;
            String validationMessage = "";
            String exampleDescription = ("".equals(splitName.getRight())) ? "description" : splitName.getRight();

            // Validate the name
            if (!prefix.getRight().isVersioned()) {
                // Must not have a version (that is, something before the separator)
                if (!"".equals(splitName.getLeft())) {
                    isValid = false;
                    validationMessage = "Invalid repeatable migration / callback name format: " + resourceName
                            + " (It cannot contain a version and should look like this: "
                            + prefixResult.getLeft() + configuration.getSqlMigrationSeparator() + exampleDescription + suffixResult.getRight() + ")";
                }
            } else {
                // Must have a version (that is, something before the separator)
                if ("".equals(splitName.getLeft())) {
                    isValid = false;
                    validationMessage = "Invalid versioned migration name format: " + resourceName
                            + " (It must contain a version and should look like this: "
                            + prefixResult.getLeft() + "1.2" + configuration.getSqlMigrationSeparator() + exampleDescription + suffixResult.getRight() + ")";
                } else {
                    // ... and that must be a legitimate version
                    try {
                        MigrationVersion.fromVersion(splitName.getLeft());
                    } catch (Exception e) {
                        isValid = false;
                        validationMessage = "Invalid versioned migration name format: " + resourceName
                                + " (could not recognise version number " + splitName.getLeft() + ")";
                    }
                }
            }

            String description = splitName.getRight().replace("_", " ");
            return new ResourceName(prefixResult.getLeft(), splitName.getLeft(),
                                    configuration.getSqlMigrationSeparator(), description, splitName.getRight(), suffixResult.getRight(),
                                    isValid, validationMessage);
        }

        // Didn't match any prefix
        return ResourceName.invalid("Unrecognised migration name format: " + resourceName);
    }


}