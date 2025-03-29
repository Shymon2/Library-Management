package Library.Project.dto.Request.User;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class UserDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String fullname;

    @NotBlank
    private String phoneNUmber;

    @NotBlank
    private String identityNum;

    @NotBlank
    private LocalDate dateOfBirth;

    private String address;

}
