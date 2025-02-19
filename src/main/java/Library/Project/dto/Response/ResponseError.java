package Library.Project.dto.Response;

import lombok.Getter;
import lombok.Setter;

@Setter
public class ResponseError extends ResponseData{

    public ResponseError(int code, String message) {
        super(code, message);
    }
}
