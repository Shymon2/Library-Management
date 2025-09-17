package Library.Project.constant.enums;

import Library.Project.configuration.Translator;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCodeSuccess implements ErrorCode{
    SUCCESS(0, Translator.toLocale("success"), HttpStatus.OK)
    ;

    ErrorCodeSuccess(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private final HttpStatusCode statusCode;

    @Override
    public String errorCode() {
        return String.valueOf(this.code);
    }
}
