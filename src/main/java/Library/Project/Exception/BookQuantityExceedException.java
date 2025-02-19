package Library.Project.Exception;

public class BookQuantityExceedException extends RuntimeException {
    public BookQuantityExceedException(String message) {
        super(message);
    }
}
