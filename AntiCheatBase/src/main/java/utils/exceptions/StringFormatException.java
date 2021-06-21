package utils.exceptions;

public class StringFormatException extends Exception {

    public StringFormatException(String message) {
        super("Error formatting string! " + message);
    }
}
