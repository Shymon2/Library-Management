package Library.Project.dto;

import Library.Project.constant.enums.ErrorCodeSuccess;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GeneralPayload<T> {
    private ResponseStatus status;
    private T data;

    public static <T> GeneralPayload<T> ofSuccess(T data){
        ResponseStatus status = ResponseStatus.of(ErrorCodeSuccess.SUCCESS);
        return GeneralPayload.<T>builder()
                .status(status)
                .data(data)
                .build();
    }
}
