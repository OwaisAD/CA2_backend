package errorhandling;

public class MissingDataException extends Exception {
    public MissingDataException(String msg) {
        super(msg);
    }
}
