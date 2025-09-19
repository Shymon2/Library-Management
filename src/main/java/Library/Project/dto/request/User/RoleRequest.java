package Library.Project.dto.request.User;

import lombok.Builder;
import lombok.Getter;

import java.util.Set;

@Getter
@Builder
public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permissions;
}
