package Library.Project.service.interfaces;

import Library.Project.dto.Request.PermissionRequest;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.PermissionResponse;

public interface IPermissionService {
    PermissionResponse create(PermissionRequest request);

    PageResponse getAll(int pageNo, int pageSize);

    void update(Long permissionId ,PermissionRequest request);

    void delete(Long permissionId);
}
