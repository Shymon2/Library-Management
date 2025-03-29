package Library.Project.service.implement;

import Library.Project.dto.Request.User.PermissionRequest;
import Library.Project.dto.Request.User.RoleRequest;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.dto.Response.UserResponse.RoleResponse;
import Library.Project.entity.Permission;
import Library.Project.entity.Role;
import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import Library.Project.repository.RoleRepository;
import Library.Project.service.interfaces.IRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleService implements IRoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public RoleResponse createNewRole(RoleRequest request) {
        if(!roleRepository.existsByName(request.getName())){
            Role newRole = Role.builder()
                    .name(request.getName())
                    .description(request.getDescription())
                    .build();
            Set<Permission> permissions = new HashSet<>();

            request.getPermissions().forEach(a -> {
                if(permissionService.existsByName(a))
                    permissions.add(permissionService.getByName(a));
                else{
                    permissionService.create(PermissionRequest.builder()
                                    .name(a)
                                    .description("None")
                            .build());
                    permissions.add(permissionService.getByName(a));
                }
            });

            newRole.setPermissions(permissions);
            roleRepository.save(newRole);
        }
        else {
            Role role = roleRepository.findByName(request.getName());
            role.setDelete(false);
            roleRepository.save(role);
        }
        return RoleResponse.builder()
                .name(request.getName())
                .description(request.getDescription())
                .permissions(request.getPermissions())
                .build();
    }

    @Override
    public PageResponse showAll(int pageNo, int pageSize) {
        Page<Role> roles = roleRepository.findByIsDelete(PageRequest.of(pageNo - 1, pageSize), false);

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(roles.getTotalPages())
                .items(roles.getContent())
                .build();
    }

    @Override
    public void updateRole(Long roleId, RoleRequest request) {
        Role role = findById(roleId);
        role.setName(request.getName());
        role.setDescription(request.getDescription());

        Set<Permission> permissions = new HashSet<>();

        request.getPermissions().forEach(a -> {
            if(permissionService.existsByName(a))
                permissions.add(permissionService.getByName(a));
            else{
                permissionService.create(PermissionRequest.builder()
                        .name(a)
                        .description("None")
                        .build());
                permissions.add(permissionService.getByName(a));
            }
        });

        role.setPermissions(permissions);
        roleRepository.save(role);
    }

    @Override
    public void deleteRole(Long roleId) {
        Role role = findById(roleId);
        role.setDelete(true);
        roleRepository.save(role);
    }

    public boolean checkExits(String name){
        return roleRepository.existsByName(name);
    }

    public Role findByName(String name){
        if(checkExits(name))
            throw new AppException(ErrorCode.ALREADY_EXISTED);
        else
            return roleRepository.findByName(name);
    }

    public Role findById(Long id){
        return roleRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
    }
}
