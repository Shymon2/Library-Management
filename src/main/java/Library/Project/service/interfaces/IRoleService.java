package Library.Project.service.interfaces;

import Library.Project.dto.Request.User.RoleRequest;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.dto.Response.UserResponse.RoleResponse;

public interface IRoleService {
    RoleResponse createNewRole(RoleRequest request);

    PageResponse showAll(int pageNo, int pageSize);

    void updateRole(Long roleId, RoleRequest request);

    void deleteRole(Long roleId);
}
