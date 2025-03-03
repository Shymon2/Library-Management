package Library.Project.service.implement;

import Library.Project.dto.Request.PermissionRequest;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.PermissionResponse;
import Library.Project.entity.Permission;
import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import Library.Project.repository.PermissionRepository;
import Library.Project.service.interfaces.IPermissionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionService implements IPermissionService {
    private final PermissionRepository permissionRepository;

    @Override
    public PermissionResponse create(PermissionRequest request) {
        if(!permissionRepository.existsByName(request.getName())){
            Permission newPermission = Permission.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
            permissionRepository.save(newPermission);
            return convertIntoResponse(newPermission);
        }
        else{
            Permission permission = permissionRepository.findByName(request.getName());
            permission.setDelete(false);
            permissionRepository.save(permission);
            return convertIntoResponse(permission);
        }
    }

    @Override
    public PageResponse getAll(int pageNo, int pageSize) {
        Page<Permission> permissions = permissionRepository.findPermissionByIsDelete(PageRequest.of(pageNo - 1, pageSize), false);
        if (permissions.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(permissions.getTotalPages())
                .items(permissions.getContent())
                .build();
    }

    @Override
    public void update(Long permissionId, PermissionRequest request) {
        Permission itemFound = permissionRepository.findById(permissionId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        itemFound.setName(request.getName());
        itemFound.setDescription(request.getDescription());
        permissionRepository.save(itemFound);
    }

    @Override
    public void delete(Long permissionId) {
        Permission itemFound = permissionRepository.findById(permissionId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        itemFound.setDelete(true);
        permissionRepository.save(itemFound);
    }

    public PermissionResponse convertIntoResponse(Permission permission){
        return PermissionResponse.builder()
                .id(permission.getId())
                .name(permission.getName())
                .description(permission.getDescription())
                .build();
    }

    public boolean existsByName(String name){
        return permissionRepository.existsByName(name);
    }

    public Permission getByName(String name){
        return permissionRepository.findByName(name);
    }
}
