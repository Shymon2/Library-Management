package Library.Project.exception;

import Library.Project.enums.ErrorCode;
import Library.Project.dto.Response.ResponseError;
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
        return new ResponseError(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode(), e.getMessage());
    }

    @ExceptionHandler(AppException.class)
    ResponseError appExceptionHandler(AppException e){
        return new ResponseError(e.getErrorCode().getCode(), e.getMessage());
    }

    @ExceptionHandler({ParseException.class, JOSEException.class})
    ResponseError introspectExceptionHandler(Exception e){
        return new ResponseError(ErrorCode.INTROSPECT_FAIL.getCode(), e.getMessage());
    }

    @ExceptionHandler(AccessDeniedException.class)
    ResponseError AccessDeniedHandler(AccessDeniedException e){
        return new ResponseError(ErrorCode.UNAUTHORIZED.getCode(), ErrorCode.UNAUTHORIZED.getMessage());
    }
}
