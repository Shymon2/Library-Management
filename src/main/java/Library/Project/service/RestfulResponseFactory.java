package Library.Project.service;

import Library.Project.dto.GeneralPayload;
import Library.Project.dto.ResponseStatus;
import org.springframework.http.ResponseEntity;

public class RestfulResponseFactory {
    private RestfulResponseFactory(){}

    public static <T> ResponseEntity<GeneralPayload<T>> of(T data){
        return ResponseEntity.ok()
                .body(GeneralPayload.ofSuccess(data));
    }

    public static <T>ResponseEntity<GeneralPayload<T>> of(ResponseStatus status, T data){
        return ResponseEntity.ok()
                .body(GeneralPayload.<T>builder()
                        .status(status)
                        .data(data)
                        .build());
    }
}
