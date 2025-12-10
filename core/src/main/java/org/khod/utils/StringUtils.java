package org.khod.utils;

public class StringUtils {

    public static boolean charSequenceEquals(final CharSequence first, final CharSequence second) {
        if(first.length() != second.length()) {
            return false;
        }
        for (int i = 0; i < first.length(); i++) {
            if (first.charAt(i) != second.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}
