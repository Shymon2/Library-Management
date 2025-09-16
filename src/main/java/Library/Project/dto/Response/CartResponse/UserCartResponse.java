package Library.Project.dto.Response.CartResponse;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class UserCartResponse implements Serializable {
    private String username;
    private String fullname;

    @NotBlank
    private String phoneNumber;

    @NotBlank
    private String identityNum;
    private LocalDate dateOfBirth;
    private String address;

    private List<CartItemResponse> listItems;
}
