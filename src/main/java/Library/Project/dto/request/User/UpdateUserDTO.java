package Library.Project.dto.request.User;

import jakarta.validation.constraints.NotBlank;
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
