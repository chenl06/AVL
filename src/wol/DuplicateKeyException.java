package wol;

/**
 * @author Chen
 * @author Edward
 * Exception thrown when a key is already in the map
 */
public class DuplicateKeyException extends RuntimeException {
    public DuplicateKeyException() {
        super();
    }

    public DuplicateKeyException(String message) {
        super(message);
    }
}
