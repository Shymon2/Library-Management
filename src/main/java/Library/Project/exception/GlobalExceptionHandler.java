package Library.Project.exception;

import Library.Project.constant.enums.ErrorCodeFail;
import Library.Project.dto.response.ApiResponse.ResponseError;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    ResponseError runtimeExceptionHandler(RuntimeException e){
        return new ResponseError(ErrorCodeFail.UNCATEGORIZED_EXCEPTION.getCode(), e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    ResponseError appExceptionHandler(AppException e){
        return new ResponseError(e.getErrorCodeFail().getCode(), e.getMessage());
    }

    @ExceptionHandler({ParseException.class, JOSEException.class})
    ResponseError introspectExceptionHandler(Exception e){
        return new ResponseError(ErrorCodeFail.INTROSPECT_FAIL.getCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseError accessDeniedHandler(AccessDeniedException e){
        return new ResponseError(ErrorCodeFail.UNAUTHORIZED.getCode(), ErrorCodeFail.UNAUTHORIZED.getMessage());
    }
}
