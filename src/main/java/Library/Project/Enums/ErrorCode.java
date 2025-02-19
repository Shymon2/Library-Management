package Library.Project.Enums;

import Library.Project.Configuration.Translator;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, "User existed", HttpStatus.BAD_REQUEST),
    NOT_FOUND(1003, "Book not found", HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1004, "You do not have permission", HttpStatus.FORBIDDEN),
    INTROSPECT_FAIL(1005, "Verify token have problem", HttpStatus.BAD_REQUEST),
    BOOK_QUANTITY_EXCEED(1006, "", HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
