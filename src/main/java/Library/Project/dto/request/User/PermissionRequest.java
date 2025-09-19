package Library.Project.dto.request.User;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionRequest {
    private String name;

    private String description;
}
