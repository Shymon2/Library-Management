package Library.Project.dto.Request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UpdateUserDTO {
    private String fullname;

    @NotBlank
    private String phoneNUmber;

    @NotBlank
    private LocalDate dateOfBirth;

    private String address;
}
