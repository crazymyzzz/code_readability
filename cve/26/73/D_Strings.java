@@ -16,6 +16,7 @@
 package io.jsonwebtoken.lang;
 
 import java.nio.charset.Charset;
+import java.nio.charset.StandardCharsets;
 import java.util.ArrayList;
 import java.util.Arrays;
 import java.util.Collection;
@@ -29,8 +30,17 @@ import java.util.Set;
 import java.util.StringTokenizer;
 import java.util.TreeSet;
 
+/**
+ * Utility methods for working with Strings to reduce pattern repetition and otherwise
+ * increased cyclomatic complexity.
+ */
 public final class Strings {
 
+    /**
+     * Empty String, equal to <code>&quot;&quot;</code>.
+     */
+    public static final String EMPTY = "";
+
     private static final String FOLDER_SEPARATOR = "/";
 
     private static final String WINDOWS_FOLDER_SEPARATOR = "\\";
@@ -41,9 +51,13 @@ public final class Strings {
 
     private static final char EXTENSION_SEPARATOR = '.';
 
-    public static final Charset UTF_8 = Charset.forName("UTF-8");
+    /**
+     * Convenience alias for {@link StandardCharsets#UTF_8}.
+     */
+    public static final Charset UTF_8 = StandardCharsets.UTF_8;
 
-    private Strings(){} //prevent instantiation
+    private Strings() {
+    } //prevent instantiation
 
     //---------------------------------------------------------------------
     // General convenience methods for working with Strings
@@ -58,6 +72,7 @@ public final class Strings {
      * Strings.hasLength(" ") = true
      * Strings.hasLength("Hello") = true
      * </pre>
+     *
      * @param str the CharSequence to check (may be <code>null</code>)
      * @return <code>true</code> if the CharSequence is not null and has length
      * @see #hasText(String)
@@ -69,6 +84,7 @@ public final class Strings {
     /**
      * Check that the given String is neither <code>null</code> nor of length 0.
      * Note: Will return <code>true</code> for a String that purely consists of whitespace.
+     *
      * @param str the String to check (may be <code>null</code>)
      * @return <code>true</code> if the String is not null and has length
      * @see #hasLength(CharSequence)
@@ -88,6 +104,7 @@ public final class Strings {
      * Strings.hasText("12345") = true
      * Strings.hasText(" 12345 ") = true
      * </pre>
+     *
      * @param str the CharSequence to check (may be <code>null</code>)
      * @return <code>true</code> if the CharSequence is not <code>null</code>,
      * its length is greater than 0, and it does not contain whitespace only
@@ -110,6 +127,7 @@ public final class Strings {
      * Check whether the given String has actual text.
      * More specifically, returns <code>true</code> if the string not <code>null</code>,
      * its length is greater than 0, and it contains at least one non-whitespace character.
+     *
      * @param str the String to check (may be <code>null</code>)
      * @return <code>true</code> if the String is not <code>null</code>, its length is
      * greater than 0, and it does not contain whitespace only
@@ -121,6 +139,7 @@ public final class Strings {
 
     /**
      * Check whether the given CharSequence contains any whitespace characters.
+     *
      * @param str the CharSequence to check (may be <code>null</code>)
      * @return <code>true</code> if the CharSequence is not empty and
      * contains at least 1 whitespace character
@@ -141,6 +160,7 @@ public final class Strings {
 
     /**
      * Check whether the given String contains any whitespace characters.
+     *
      * @param str the String to check (may be <code>null</code>)
      * @return <code>true</code> if the String is not empty and
      * contains at least 1 whitespace character
@@ -152,15 +172,16 @@ public final class Strings {
 
     /**
      * Trim leading and trailing whitespace from the given String.
+     *
      * @param str the String to check
      * @return the trimmed String
      * @see java.lang.Character#isWhitespace
      */
     public static String trimWhitespace(String str) {
-        return (String) trimWhitespace((CharSequence)str);
+        return (String) trimWhitespace((CharSequence) str);
     }
-    
-    
+
+
     private static CharSequence trimWhitespace(CharSequence str) {
         if (!hasLength(str)) {
             return str;
@@ -168,24 +189,40 @@ public final class Strings {
         final int length = str.length();
 
         int start = 0;
-		while (start < length && Character.isWhitespace(str.charAt(start))) {
+        while (start < length && Character.isWhitespace(str.charAt(start))) {
             start++;
         }
-        
-		int end = length;
+
+        int end = length;
         while (start < length && Character.isWhitespace(str.charAt(end - 1))) {
             end--;
         }
-        
+
         return ((start > 0) || (end < length)) ? str.subSequence(start, end) : str;
     }
 
+    /**
+     * Returns the specified string without leading or trailing whitespace, or {@code null} if there are no remaining
+     * characters.
+     *
+     * @param str the string to clean
+     * @return the specified string without leading or trailing whitespace, or {@code null} if there are no remaining
+     * characters.
+     */
     public static String clean(String str) {
-    	CharSequence result = clean((CharSequence) str);
-        
-        return result!=null?result.toString():null;
+        CharSequence result = clean((CharSequence) str);
+
+        return result != null ? result.toString() : null;
     }
-    
+
+    /**
+     * Returns the specified {@code CharSequence} without leading or trailing whitespace, or {@code null} if there are
+     * no remaining characters.
+     *
+     * @param str the {@code CharSequence} to clean
+     * @return the specified string without leading or trailing whitespace, or {@code null} if there are no remaining
+     * characters.
+     */
     public static CharSequence clean(CharSequence str) {
         str = trimWhitespace(str);
         if (!hasLength(str)) {
@@ -194,9 +231,56 @@ public final class Strings {
         return str;
     }
 
+    /**
+     * Returns a String representation (1s and 0s) of the specified byte.
+     *
+     * @param b the byte to represent as 1s and 0s.
+     * @return a String representation (1s and 0s) of the specified byte.
+     */
+    public static String toBinary(byte b) {
+        String bString = Integer.toBinaryString(b & 0xFF);
+        return String.format("%8s", bString).replace((char) Character.SPACE_SEPARATOR, '0');
+    }
+
+    /**
+     * Returns a String representation (1s and 0s) of the specified byte array.
+     *
+     * @param bytes the bytes to represent as 1s and 0s.
+     * @return a String representation (1s and 0s) of the specified byte array.
+     */
+    public static String toBinary(byte[] bytes) {
+        StringBuilder sb = new StringBuilder(19); //16 characters + 3 space characters
+        for (byte b : bytes) {
+            if (sb.length() > 0) {
+                sb.append((char) Character.SPACE_SEPARATOR);
+            }
+            String val = toBinary(b);
+            sb.append(val);
+        }
+        return sb.toString();
+    }
+
+    /**
+     * Returns a hexadecimal String representation of the specified byte array.
+     *
+     * @param bytes the bytes to represent as a hexidecimal string.
+     * @return a hexadecimal String representation of the specified byte array.
+     */
+    public static String toHex(byte[] bytes) {
+        StringBuilder result = new StringBuilder();
+        for (byte temp : bytes) {
+            if (result.length() > 0) {
+                result.append((char) Character.SPACE_SEPARATOR);
+            }
+            result.append(String.format("%02x", temp));
+        }
+        return result.toString();
+    }
+
     /**
      * Trim <i>all</i> whitespace from the given String:
      * leading, trailing, and intermediate characters.
+     *
      * @param str the String to check
      * @return the trimmed String
      * @see java.lang.Character#isWhitespace
@@ -210,8 +294,7 @@ public final class Strings {
         while (sb.length() > index) {
             if (Character.isWhitespace(sb.charAt(index))) {
                 sb.deleteCharAt(index);
-            }
-            else {
+            } else {
                 index++;
             }
         }
@@ -220,6 +303,7 @@ public final class Strings {
 
     /**
      * Trim leading whitespace from the given String.
+     *
      * @param str the String to check
      * @return the trimmed String
      * @see java.lang.Character#isWhitespace
@@ -237,6 +321,7 @@ public final class Strings {
 
     /**
      * Trim trailing whitespace from the given String.
+     *
      * @param str the String to check
      * @return the trimmed String
      * @see java.lang.Character#isWhitespace
@@ -254,7 +339,8 @@ public final class Strings {
 
     /**
      * Trim all occurrences of the supplied leading character from the given String.
-     * @param str the String to check
+     *
+     * @param str              the String to check
      * @param leadingCharacter the leading character to be trimmed
      * @return the trimmed String
      */
@@ -271,7 +357,8 @@ public final class Strings {
 
     /**
      * Trim all occurrences of the supplied trailing character from the given String.
-     * @param str the String to check
+     *
+     * @param str               the String to check
      * @param trailingCharacter the trailing character to be trimmed
      * @return the trimmed String
      */
@@ -289,7 +376,8 @@ public final class Strings {
 
     /**
      * Returns {@code true} if the given string starts with the specified case-insensitive prefix, {@code false} otherwise.
-     * @param str the String to check
+     *
+     * @param str    the String to check
      * @param prefix the prefix to look for
      * @return {@code true} if the given string starts with the specified case-insensitive prefix, {@code false} otherwise.
      * @see java.lang.String#startsWith
@@ -298,12 +386,12 @@ public final class Strings {
         if (str == null || prefix == null) {
             return false;
         }
-        if (str.startsWith(prefix)) {
-            return true;
-        }
         if (str.length() < prefix.length()) {
             return false;
         }
+        if (str.startsWith(prefix)) {
+            return true;
+        }
         String lcStr = str.substring(0, prefix.length()).toLowerCase();
         String lcPrefix = prefix.toLowerCase();
         return lcStr.equals(lcPrefix);
@@ -311,7 +399,8 @@ public final class Strings {
 
     /**
      * Returns {@code true} if the given string ends with the specified case-insensitive suffix, {@code false} otherwise.
-     * @param str the String to check
+     *
+     * @param str    the String to check
      * @param suffix the suffix to look for
      * @return {@code true} if the given string ends with the specified case-insensitive suffix, {@code false} otherwise.
      * @see java.lang.String#endsWith
@@ -334,8 +423,9 @@ public final class Strings {
 
     /**
      * Returns {@code true} if the given string matches the given substring at the given index, {@code false} otherwise.
-     * @param str the original string (or StringBuilder)
-     * @param index the index in the original string to start matching against
+     *
+     * @param str       the original string (or StringBuilder)
+     * @param index     the index in the original string to start matching against
      * @param substring the substring to match at the given index
      * @return {@code true} if the given string matches the given substring at the given index, {@code false} otherwise.
      */
@@ -351,6 +441,7 @@ public final class Strings {
 
     /**
      * Returns the number of occurrences the substring {@code sub} appears in string {@code str}.
+     *
      * @param str string to search in. Return 0 if this is null.
      * @param sub string to search for. Return 0 if this is null.
      * @return the number of occurrences the substring {@code sub} appears in string {@code str}.
@@ -372,7 +463,8 @@ public final class Strings {
     /**
      * Replace all occurrences of a substring within a string with
      * another string.
-     * @param inString String to examine
+     *
+     * @param inString   String to examine
      * @param oldPattern String to replace
      * @param newPattern String to insert
      * @return a String with the replacements
@@ -399,8 +491,9 @@ public final class Strings {
 
     /**
      * Delete all occurrences of the given substring.
+     *
      * @param inString the original String
-     * @param pattern the pattern to delete all occurrences of
+     * @param pattern  the pattern to delete all occurrences of
      * @return the resulting String
      */
     public static String delete(String inString, String pattern) {
@@ -409,9 +502,10 @@ public final class Strings {
 
     /**
      * Delete any character in a given String.
-     * @param inString the original String
+     *
+     * @param inString      the original String
      * @param charsToDelete a set of characters to delete.
-     * E.g. "az\n" will delete 'a's, 'z's and new lines.
+     *                      E.g. "az\n" will delete 'a's, 'z's and new lines.
      * @return the resulting String
      */
     public static String deleteAny(String inString, String charsToDelete) {
@@ -435,6 +529,7 @@ public final class Strings {
 
     /**
      * Quote the given String with single quotes.
+     *
      * @param str the input String (e.g. "myString")
      * @return the quoted String (e.g. "'myString'"),
      * or <code>null</code> if the input was <code>null</code>
@@ -446,6 +541,7 @@ public final class Strings {
     /**
      * Turn the given Object into a String with single quotes
      * if it is a String; keeping the Object as-is else.
+     *
      * @param obj the input Object (e.g. "myString")
      * @return the quoted String (e.g. "'myString'"),
      * or the input object as-is if not a String
@@ -457,6 +553,7 @@ public final class Strings {
     /**
      * Unqualify a string qualified by a '.' dot character. For example,
      * "this.name.is.qualified", returns "qualified".
+     *
      * @param qualifiedName the qualified name
      * @return an unqualified string by stripping all previous text before (and including) the last period character.
      */
@@ -467,8 +564,9 @@ public final class Strings {
     /**
      * Unqualify a string qualified by a separator character. For example,
      * "this:name:is:qualified" returns "qualified" if using a ':' separator.
+     *
      * @param qualifiedName the qualified name
-     * @param separator the separator
+     * @param separator     the separator
      * @return an unqualified string by stripping all previous text before and including the last {@code separator} character.
      */
     public static String unqualify(String qualifiedName, char separator) {
@@ -479,6 +577,7 @@ public final class Strings {
      * Capitalize a <code>String</code>, changing the first letter to
      * upper case as per {@link Character#toUpperCase(char)}.
      * No other letters are changed.
+     *
      * @param str the String to capitalize, may be <code>null</code>
      * @return the capitalized String, <code>null</code> if null
      */
@@ -490,6 +589,7 @@ public final class Strings {
      * Uncapitalize a <code>String</code>, changing the first letter to
      * lower case as per {@link Character#toLowerCase(char)}.
      * No other letters are changed.
+     *
      * @param str the String to uncapitalize, may be <code>null</code>
      * @return the uncapitalized String, <code>null</code> if null
      */
@@ -504,8 +604,7 @@ public final class Strings {
         StringBuilder sb = new StringBuilder(str.length());
         if (capitalize) {
             sb.append(Character.toUpperCase(str.charAt(0)));
-        }
-        else {
+        } else {
             sb.append(Character.toLowerCase(str.charAt(0)));
         }
         sb.append(str.substring(1));
@@ -515,6 +614,7 @@ public final class Strings {
     /**
      * Extract the filename from the given path,
      * e.g. "mypath/myfile.txt" -&gt; "myfile.txt".
+     *
      * @param path the file path (may be <code>null</code>)
      * @return the extracted filename, or <code>null</code> if none
      */
@@ -529,6 +629,7 @@ public final class Strings {
     /**
      * Extract the filename extension from the given path,
      * e.g. "mypath/myfile.txt" -&gt; "txt".
+     *
      * @param path the file path (may be <code>null</code>)
      * @return the extracted filename extension, or <code>null</code> if none
      */
@@ -550,6 +651,7 @@ public final class Strings {
     /**
      * Strip the filename extension from the given path,
      * e.g. "mypath/myfile.txt" -&gt; "mypath/myfile".
+     *
      * @param path the file path (may be <code>null</code>)
      * @return the path with stripped filename extension,
      * or <code>null</code> if none
@@ -572,9 +674,10 @@ public final class Strings {
     /**
      * Apply the given relative path to the given path,
      * assuming standard Java folder separation (i.e. "/" separators).
-     * @param path the path to start from (usually a full file path)
+     *
+     * @param path         the path to start from (usually a full file path)
      * @param relativePath the relative path to apply
-     * (relative to the full file path above)
+     *                     (relative to the full file path above)
      * @return the full file path that results from applying the relative path
      */
     public static String applyRelativePath(String path, String relativePath) {
@@ -585,8 +688,7 @@ public final class Strings {
                 newPath += FOLDER_SEPARATOR;
             }
             return newPath + relativePath;
-        }
-        else {
+        } else {
             return relativePath;
         }
     }
@@ -596,6 +698,7 @@ public final class Strings {
      * inner simple dots.
      * <p>The result is convenient for path comparison. For other uses,
      * notice that Windows separators ("\") are replaced by simple slashes.
+     *
      * @param path the original path
      * @return the normalized path
      */
@@ -628,17 +731,14 @@ public final class Strings {
             String element = pathArray[i];
             if (CURRENT_PATH.equals(element)) {
                 // Points to current directory - drop it.
-            }
-            else if (TOP_PATH.equals(element)) {
+            } else if (TOP_PATH.equals(element)) {
                 // Registering top path found.
                 tops++;
-            }
-            else {
+            } else {
                 if (tops > 0) {
                     // Merging path element with element corresponding to top path.
                     tops--;
-                }
-                else {
+                } else {
                     // Normal path element found.
                     pathElements.add(0, element);
                 }
@@ -655,6 +755,7 @@ public final class Strings {
 
     /**
      * Compare two paths after normalization of them.
+     *
      * @param path1 first path for comparison
      * @param path2 second path for comparison
      * @return whether the two paths are equivalent after normalization
@@ -666,9 +767,10 @@ public final class Strings {
     /**
      * Parse the given <code>localeString</code> value into a {@link java.util.Locale}.
      * <p>This is the inverse operation of {@link java.util.Locale#toString Locale's toString}.
+     *
      * @param localeString the locale string, following <code>Locale's</code>
-     * <code>toString()</code> format ("en", "en_UK", etc);
-     * also accepts spaces as separators, as an alternative to underscores
+     *                     <code>toString()</code> format ("en", "en_UK", etc);
+     *                     also accepts spaces as separators, as an alternative to underscores
      * @return a corresponding <code>Locale</code> instance
      */
     public static Locale parseLocaleString(String localeString) {
@@ -696,7 +798,7 @@ public final class Strings {
             char ch = localePart.charAt(i);
             if (ch != '_' && ch != ' ' && !Character.isLetterOrDigit(ch)) {
                 throw new IllegalArgumentException(
-                    "Locale part \"" + localePart + "\" contains invalid characters");
+                        "Locale part \"" + localePart + "\" contains invalid characters");
             }
         }
     }
@@ -704,6 +806,7 @@ public final class Strings {
     /**
      * Determine the RFC 3066 compliant language tag,
      * as used for the HTTP "Accept-Language" header.
+     *
      * @param locale the Locale to transform to a language tag
      * @return the RFC 3066 compliant language tag as String
      */
@@ -719,13 +822,14 @@ public final class Strings {
     /**
      * Append the given String to the given String array, returning a new array
      * consisting of the input array contents plus the given String.
+     *
      * @param array the array to append to (can be <code>null</code>)
-     * @param str the String to append
+     * @param str   the String to append
      * @return the new array (never <code>null</code>)
      */
     public static String[] addStringToArray(String[] array, String str) {
         if (Objects.isEmpty(array)) {
-            return new String[] {str};
+            return new String[]{str};
         }
         String[] newArr = new String[array.length + 1];
         System.arraycopy(array, 0, newArr, 0, array.length);
@@ -737,6 +841,7 @@ public final class Strings {
      * Concatenate the given String arrays into one,
      * with overlapping array elements included twice.
      * <p>The order of elements in the original arrays is preserved.
+     *
      * @param array1 the first array (can be <code>null</code>)
      * @param array2 the second array (can be <code>null</code>)
      * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
@@ -760,6 +865,7 @@ public final class Strings {
      * <p>The order of elements in the original arrays is preserved
      * (with the exception of overlapping elements, which are only
      * included on their first occurrence).
+     *
      * @param array1 the first array (can be <code>null</code>)
      * @param array2 the second array (can be <code>null</code>)
      * @return the new array (<code>null</code> if both given arrays were <code>null</code>)
@@ -783,6 +889,7 @@ public final class Strings {
 
     /**
      * Turn given source String array into sorted array.
+     *
      * @param array the source array
      * @return the sorted array (never <code>null</code>)
      */
@@ -797,6 +904,7 @@ public final class Strings {
     /**
      * Copy the given Collection into a String array.
      * The Collection must contain String elements only.
+     *
      * @param collection the Collection to copy
      * @return the String array (<code>null</code> if the passed-in
      * Collection was <code>null</code>)
@@ -811,6 +919,7 @@ public final class Strings {
     /**
      * Copy the given Enumeration into a String array.
      * The Enumeration must contain String elements only.
+     *
      * @param enumeration the Enumeration to copy
      * @return the String array (<code>null</code> if the passed-in
      * Enumeration was <code>null</code>)
@@ -826,6 +935,7 @@ public final class Strings {
     /**
      * Trim the elements of the given String array,
      * calling <code>String.trim()</code> on each of them.
+     *
      * @param array the original String array
      * @return the resulting array (of the same size) with trimmed elements
      */
@@ -844,6 +954,7 @@ public final class Strings {
     /**
      * Remove duplicate Strings from the given array.
      * Also sorts the array, as it uses a TreeSet.
+     *
      * @param array the String array
      * @return an array without duplicates, in natural sort order
      */
@@ -861,7 +972,8 @@ public final class Strings {
     /**
      * Split a String at the first occurrence of the delimiter.
      * Does not include the delimiter in the result.
-     * @param toSplit the string to split
+     *
+     * @param toSplit   the string to split
      * @param delimiter to split the string up with
      * @return a two element array with index 0 being before the delimiter, and
      * index 1 being after the delimiter (neither element includes the delimiter);
@@ -877,7 +989,7 @@ public final class Strings {
         }
         String beforeDelimiter = toSplit.substring(0, offset);
         String afterDelimiter = toSplit.substring(offset + delimiter.length());
-        return new String[] {beforeDelimiter, afterDelimiter};
+        return new String[]{beforeDelimiter, afterDelimiter};
     }
 
     /**
@@ -886,7 +998,8 @@ public final class Strings {
      * delimiter providing the key, and the right of the delimiter providing the value.
      * <p>Will trim both the key and value before adding them to the
      * <code>Properties</code> instance.
-     * @param array the array to process
+     *
+     * @param array     the array to process
      * @param delimiter to split each element using (typically the equals symbol)
      * @return a <code>Properties</code> instance representing the array contents,
      * or <code>null</code> if the array to process was null or empty
@@ -901,16 +1014,17 @@ public final class Strings {
      * delimiter providing the key, and the right of the delimiter providing the value.
      * <p>Will trim both the key and value before adding them to the
      * <code>Properties</code> instance.
-     * @param array the array to process
-     * @param delimiter to split each element using (typically the equals symbol)
+     *
+     * @param array         the array to process
+     * @param delimiter     to split each element using (typically the equals symbol)
      * @param charsToDelete one or more characters to remove from each element
-     * prior to attempting the split operation (typically the quotation mark
-     * symbol), or <code>null</code> if no removal should occur
+     *                      prior to attempting the split operation (typically the quotation mark
+     *                      symbol), or <code>null</code> if no removal should occur
      * @return a <code>Properties</code> instance representing the array contents,
      * or <code>null</code> if the array to process was <code>null</code> or empty
      */
     public static Properties splitArrayElementsIntoProperties(
-        String[] array, String delimiter, String charsToDelete) {
+            String[] array, String delimiter, String charsToDelete) {
 
         if (Objects.isEmpty(array)) {
             return null;
@@ -936,9 +1050,10 @@ public final class Strings {
      * delimiter characters. Each of those characters can be used to separate
      * tokens. A delimiter is always a single character; for multi-character
      * delimiters, consider using <code>delimitedListToStringArray</code>
-     * @param str the String to tokenize
+     *
+     * @param str        the String to tokenize
      * @param delimiters the delimiter characters, assembled as String
-     * (each of those characters is individually considered as delimiter).
+     *                   (each of those characters is individually considered as delimiter).
      * @return an array of the tokens
      * @see java.util.StringTokenizer
      * @see java.lang.String#trim()
@@ -954,13 +1069,14 @@ public final class Strings {
      * delimiter characters. Each of those characters can be used to separate
      * tokens. A delimiter is always a single character; for multi-character
      * delimiters, consider using <code>delimitedListToStringArray</code>
-     * @param str the String to tokenize
-     * @param delimiters the delimiter characters, assembled as String
-     * (each of those characters is individually considered as delimiter)
-     * @param trimTokens trim the tokens via String's <code>trim</code>
+     *
+     * @param str               the String to tokenize
+     * @param delimiters        the delimiter characters, assembled as String
+     *                          (each of those characters is individually considered as delimiter)
+     * @param trimTokens        trim the tokens via String's <code>trim</code>
      * @param ignoreEmptyTokens omit empty tokens from the result array
-     * (only applies to tokens that are empty after trimming; StringTokenizer
-     * will not consider subsequent delimiters as token in the first place).
+     *                          (only applies to tokens that are empty after trimming; StringTokenizer
+     *                          will not consider subsequent delimiters as token in the first place).
      * @return an array of the tokens (<code>null</code> if the input String
      * was <code>null</code>)
      * @see java.util.StringTokenizer
@@ -968,7 +1084,7 @@ public final class Strings {
      * @see #delimitedListToStringArray
      */
     public static String[] tokenizeToStringArray(
-        String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
+            String str, String delimiters, boolean trimTokens, boolean ignoreEmptyTokens) {
 
         if (str == null) {
             return null;
@@ -992,9 +1108,10 @@ public final class Strings {
      * <p>A single delimiter can consists of more than one character: It will still
      * be considered as single delimiter string, rather than as bunch of potential
      * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
-     * @param str the input String
+     *
+     * @param str       the input String
      * @param delimiter the delimiter between elements (this is a single delimiter,
-     * rather than a bunch individual delimiter characters)
+     *                  rather than a bunch individual delimiter characters)
      * @return an array of the tokens in the list
      * @see #tokenizeToStringArray
      */
@@ -1007,11 +1124,12 @@ public final class Strings {
      * <p>A single delimiter can consists of more than one character: It will still
      * be considered as single delimiter string, rather than as bunch of potential
      * delimiter characters - in contrast to <code>tokenizeToStringArray</code>.
-     * @param str the input String
-     * @param delimiter the delimiter between elements (this is a single delimiter,
-     * rather than a bunch individual delimiter characters)
+     *
+     * @param str           the input String
+     * @param delimiter     the delimiter between elements (this is a single delimiter,
+     *                      rather than a bunch individual delimiter characters)
      * @param charsToDelete a set of characters to delete. Useful for deleting unwanted
-     * line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
+     *                      line breaks: e.g. "\r\n\f" will delete all new lines and line feeds in a String.
      * @return an array of the tokens in the list
      * @see #tokenizeToStringArray
      */
@@ -1020,15 +1138,14 @@ public final class Strings {
             return new String[0];
         }
         if (delimiter == null) {
-            return new String[] {str};
+            return new String[]{str};
         }
         List<String> result = new ArrayList<String>();
         if ("".equals(delimiter)) {
             for (int i = 0; i < str.length(); i++) {
                 result.add(deleteAny(str.substring(i, i + 1), charsToDelete));
             }
-        }
-        else {
+        } else {
             int pos = 0;
             int delPos;
             while ((delPos = str.indexOf(delimiter, pos)) != -1) {
@@ -1045,6 +1162,7 @@ public final class Strings {
 
     /**
      * Convert a CSV list into an array of Strings.
+     *
      * @param str the input String
      * @return an array of Strings, or the empty array in case of empty input
      */
@@ -1055,6 +1173,7 @@ public final class Strings {
     /**
      * Convenience method to convert a CSV string list to a set.
      * Note that this will suppress duplicates.
+     *
      * @param str the input String
      * @return a Set of String entries in the list
      */
@@ -1070,8 +1189,9 @@ public final class Strings {
     /**
      * Convenience method to return a Collection as a delimited (e.g. CSV)
      * String. E.g. useful for <code>toString()</code> implementations.
-     * @param coll the Collection to display
-     * @param delim the delimiter to use (probably a ",")
+     *
+     * @param coll   the Collection to display
+     * @param delim  the delimiter to use (probably a ",")
      * @param prefix the String to start each element with
      * @param suffix the String to end each element with
      * @return the delimited String
@@ -1094,7 +1214,8 @@ public final class Strings {
     /**
      * Convenience method to return a Collection as a delimited (e.g. CSV)
      * String. E.g. useful for <code>toString()</code> implementations.
-     * @param coll the Collection to display
+     *
+     * @param coll  the Collection to display
      * @param delim the delimiter to use (probably a ",")
      * @return the delimited String
      */
@@ -1105,6 +1226,7 @@ public final class Strings {
     /**
      * Convenience method to return a Collection as a CSV String.
      * E.g. useful for <code>toString()</code> implementations.
+     *
      * @param coll the Collection to display
      * @return the delimited String
      */
@@ -1115,7 +1237,8 @@ public final class Strings {
     /**
      * Convenience method to return a String array as a delimited (e.g. CSV)
      * String. E.g. useful for <code>toString()</code> implementations.
-     * @param arr the array to display
+     *
+     * @param arr   the array to display
      * @param delim the delimiter to use (probably a ",")
      * @return the delimited String
      */
@@ -1139,6 +1262,7 @@ public final class Strings {
     /**
      * Convenience method to return a String array as a CSV String.
      * E.g. useful for <code>toString()</code> implementations.
+     *
      * @param arr the array to display
      * @return the delimited String
      */
@@ -1146,5 +1270,30 @@ public final class Strings {
         return arrayToDelimitedString(arr, ",");
     }
 
+    /**
+     * Appends a space character (<code>' '</code>) if the argument is not empty, otherwise does nothing.  This method
+     * can be thought of as &quot;non-empty space&quot;.  Using this method allows reduction of this:
+     * <blockquote><pre>
+     * if (sb.length != 0) {
+     *     sb.append(' ');
+     * }
+     * sb.append(nextWord);</pre></blockquote>
+     * <p>To this:</p>
+     * <blockquote><pre>
+     * nespace(sb).append(nextWord);</pre></blockquote>
+     * @param sb the string builder to append a space to if non-empty
+     * @return the string builder argument for method chaining.
+     * @since JJWT_RELEASE_VERSION
+     */
+    public static StringBuilder nespace(StringBuilder sb) {
+        if (sb == null) {
+            return null;
+        }
+        if (sb.length() != 0) {
+            sb.append(' ');
+        }
+        return sb;
+    }
+
 }
 
