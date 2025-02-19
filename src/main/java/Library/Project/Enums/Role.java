package Library.Project.Enums;

import Library.Project.constant.RolePermissions;
import lombok.Getter;

@Getter
public enum Role{
    ADMIN(RolePermissions.admin_permission),
    USER(RolePermissions.user_permission),
    LIBRARIAN(RolePermissions.librarian_permission),
    MANAGER(RolePermissions.manager_permission);

    private final String[] permissions;


    Role(String[] permissions) {
        this.permissions = permissions;
    }
}
