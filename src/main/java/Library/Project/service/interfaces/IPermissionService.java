package Library.Project.service.interfaces;

import Library.Project.dto.request.User.PermissionRequest;
import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.dto.response.UserResponse.PermissionResponse;

public interface IPermissionService {
    PermissionResponse create(PermissionRequest request);

    PageResponse getAll(int pageNo, int pageSize);

    void update(Long permissionId ,PermissionRequest request);

    void delete(Long permissionId);
}
