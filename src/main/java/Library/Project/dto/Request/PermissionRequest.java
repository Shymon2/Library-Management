package Library.Project.dto.Request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PermissionRequest {
    private String name;

    private String description;
}
