package dev.ethans.cscbattlegrounds.util;

public class StringUtil {

    public static String evenlySpaced(String... strings) {
        int totalWidth = 35;
        int numberOfStrings = strings.length;

        // Calculate the number of spaces needed between each string
        int spacesBetweenStrings = (totalWidth - strings.length + 1) / numberOfStrings;

        // Calculate the remaining spaces to distribute
        int remainingSpaces = totalWidth - (spacesBetweenStrings * (numberOfStrings - 1));

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < numberOfStrings; i++) {
            result.append(strings[i]);

            // Append spaces between strings
            if (i < numberOfStrings - 1) {
                int spacesToAppend = spacesBetweenStrings + (i < remainingSpaces ? 1 : 0);
                result.append(" ".repeat(Math.max(0, spacesToAppend)));
            }
        }

        return result.toString();
    }
}
