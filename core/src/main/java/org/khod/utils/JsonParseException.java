package org.khod.utils;

public class JsonParseException extends Exception {
    public JsonParseException(final String message, int index) {
        super(concatMessage(message, index));
    }

    private static String concatMessage(final String message, int index) {
        return String.format("Failed to parse JSON at index %d. %s", index, message);
    }
}
