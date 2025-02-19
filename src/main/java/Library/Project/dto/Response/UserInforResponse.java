package Library.Project.dto.Response;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Builder
public class UserInforResponse implements Serializable {
    private String username;
    private String fullname;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String identityNum;
    private LocalDate dateOfBirth;
    private String address;

    private String role;
}
