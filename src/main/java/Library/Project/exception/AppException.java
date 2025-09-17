package Library.Project.exception;

import Library.Project.constant.enums.ErrorCodeFail;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppException extends RuntimeException {
    private final ErrorCodeFail errorCodeFail;

    public AppException(ErrorCodeFail errorCodeFail) {
        super(errorCodeFail.getMessage());
        this.errorCodeFail = errorCodeFail;
    }
}
