package Library.Project.dto;

import Library.Project.configuration.Translator;
import Library.Project.constant.enums.ErrorCode;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class ResponseStatus {
    private String code;
    private String message;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm:ss dd/MM/yyyy")
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();

    private String displayMessage;

    public static ResponseStatus of(ErrorCode errorCode){
        return ResponseStatus.builder()
                .code(errorCode.errorCode())
                .message(Translator.toLocale(errorCode.errorCode()))
                .displayMessage(Translator.toLocale(errorCode.errorCode()))
                .build();
    }
}
