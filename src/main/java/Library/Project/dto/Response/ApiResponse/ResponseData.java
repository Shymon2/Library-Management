package Library.Project.dto.Response.ApiResponse;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class ResponseData<T> implements Serializable {
    private final int code;
    private final String message;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private T data;


    public ResponseData(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }


    public ResponseData(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
