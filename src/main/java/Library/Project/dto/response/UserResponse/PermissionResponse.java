package Library.Project.dto.response.UserResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PermissionResponse {
    private Long id;
    private String name;
    private String description;
}
