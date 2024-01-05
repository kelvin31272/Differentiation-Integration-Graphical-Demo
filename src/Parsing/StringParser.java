package Parsing;

import java.util.ArrayList;

public class StringParser {
    public static ArrayList<String> splitByDelimiterPairs(String string, char[] setLeft, char[] setRight) {
        /*
         * splits the input string into substrings delimited by pairs of certain
         * characters specified in 'setLeft' and 'setRight'.
         */
        /*
         * To understand the output (but this isn't the actual algorithm):
         * the string is split into substrings, using delimiter(s) specified in setLeft
         * then in each substring, find the first occurance of delimiter(s) specified in
         * setRight and trim off anything to the right, including the delimiter itself
         * e.g: "(100/200)(300/40)" with left {'('} and right {')'} becomes
         * {"(100/200", "(300/40"}
         */

        ArrayList<String> strings = new ArrayList<>();
        int index = 0;
        while (index < string.length()) {
            int startIndex = findNextMatch(string, setLeft, index);
            if (startIndex == -1)
                break;

            int endIndex = findNextMatch(string, setRight, startIndex + 1);
            if (endIndex == -1)
                break;

            strings.add(string.substring(startIndex, endIndex));
            index = endIndex;
        }
        return strings;
    }

    public static int findNextMatch(String string, char[] set, int startIndex) {
        // return the index of the next occurrence of a certain character(s) in a string
        // beginning with a starting index
        for (int i = startIndex; i <= string.length(); i++) {
            if (i == string.length()) {
                return i;
            }
            if (setContains(set, string.charAt(i))) {
                return i;
            }
        }
        return -1;
    }

    public static boolean setContains(char[] arr, char target) {
        // return whether an array of characters contains a desired character
        for (char ch : arr) {
            if (ch == target) {
                return true;
            }
        }
        return false;
    }

    public static String cutFromLeft(String string, char delimiter) {
        // return a substring, from the left, to the first occurrence of a delimiter, or
        // the end of the string if the delim is not found
        int i = 0;
        for (; i < string.length(); i++) {
            if (string.charAt(i) == delimiter) {
                break;
            }
        }
        return string.substring(0, i);
    }

}
