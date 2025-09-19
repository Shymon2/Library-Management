package Library.Project.service.interfaces;

import Library.Project.dto.request.User.RoleRequest;
import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.dto.response.UserResponse.RoleResponse;

public interface IRoleService {
    RoleResponse createNewRole(RoleRequest request);

    PageResponse showAll(int pageNo, int pageSize);

    void updateRole(Long roleId, RoleRequest request);

    void deleteRole(Long roleId);
}
