package Library.Project.dto.response.UserResponse;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@Builder
public class RoleResponse {
    private String name;
    private String description;
    private Set<String> permissions;
}
