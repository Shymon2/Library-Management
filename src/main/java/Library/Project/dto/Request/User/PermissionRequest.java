package Library.Project.dto.Request.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionRequest {
    private String name;

    private String description;
}
