package Library.Project.enums;

import Library.Project.configuration.Translator;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized error", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1002, Translator.toLocale("error.user.existed"), HttpStatus.BAD_REQUEST),
    NOT_FOUND(1003, Translator.toLocale("error.not.found"), HttpStatus.BAD_REQUEST),
    UNAUTHORIZED(1004, Translator.toLocale("error.code.unauthorized"), HttpStatus.FORBIDDEN),
    INTROSPECT_FAIL(1005, Translator.toLocale("error.code.introspect"), HttpStatus.BAD_REQUEST),
    BOOK_QUANTITY_EXCEED(1006, Translator.toLocale("error.code.book.quantity"), HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1007, Translator.toLocale("error.code.unauthenticated"), HttpStatus.UNAUTHORIZED),
    USERNAME_PASSWORD_INCORRECT(1008, Translator.toLocale("error.username.password.incorrect"), HttpStatus.BAD_REQUEST),
    ALREADY_EXISTED(1009, Translator.toLocale("error.already.existed"), HttpStatus.BAD_REQUEST);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;
}
