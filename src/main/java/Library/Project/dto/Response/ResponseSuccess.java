package Library.Project.dto.Response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

public class ResponseSuccess extends ResponseEntity<ResponseSuccess.Payload> {

    //PUT, PATCH, DELETE
    public ResponseSuccess(HttpStatusCode status, String message){
        super(new Payload(status.value(), message), HttpStatus.OK);
    }

    //GET, POST
    public ResponseSuccess(HttpStatusCode status, String message, Object data){
        super(new Payload(status.value(), message, data), HttpStatus.OK);
    }

    @Getter
    @Setter
    public static class Payload{
        private final int status;
        private final String message;
        private Object responseData;

        public Payload(int status, String message) {
            this.status = status;
            this.message = message;
        }

        public Payload(int status, String message, Object data) {
            this.status = status;
            this.message = message;
            this.responseData = data;
        }

    }
}
