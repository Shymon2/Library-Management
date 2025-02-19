package Library.Project.Exception;

import Library.Project.Enums.ErrorCode;
import Library.Project.dto.Response.ResponseError;
import com.nimbusds.jose.JOSEException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.text.ParseException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = RuntimeException.class)
    ResponseError runtimeExceptionHandler(RuntimeException e){
        return new ResponseError(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = AlreadyExistsException.class)
    ResponseError alreadyExistsExceptionHandler(AlreadyExistsException e){
        return new ResponseError(ErrorCode.USER_EXISTED.getCode(), e.getMessage());
    }

    @ExceptionHandler(value = ResourcesNotFoundException.class)
    ResponseError handlingRuntimeException(ResourcesNotFoundException e) {
        return new ResponseError(ErrorCode.NOT_FOUND.getCode(), e.getMessage());
    }

    @ExceptionHandler({ParseException.class, JOSEException.class})
    ResponseError introspectExceptionHandler(Exception e){
        return new ResponseError(ErrorCode.INTROSPECT_FAIL.getCode(), e.getMessage());
    }

    @ExceptionHandler(BookQuantityExceedException.class)
    ResponseError BookQuantityHandler(Exception e){
        return new ResponseError(ErrorCode.BOOK_QUANTITY_EXCEED.getCode(), e.getMessage());
    }
}
