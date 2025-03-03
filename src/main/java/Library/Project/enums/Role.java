package Library.Project.enums;

import Library.Project.constant.RolePermissions;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Role{
    ADMIN(RolePermissions.admin_permission, "ADMIN"),
    USER(RolePermissions.user_permission, "USER"),
    LIBRARIAN(RolePermissions.librarian_permission, "LIBRARIAN"),
    MANAGER(RolePermissions.manager_permission, "MANAGER");

    private final String[] permissions;
    private final String name;

}
