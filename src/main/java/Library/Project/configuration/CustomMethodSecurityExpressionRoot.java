package Library.Project.configuration;

import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.expression.SecurityExpressionRoot;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

@Slf4j
public class CustomMethodSecurityExpressionRoot extends SecurityExpressionRoot implements MethodSecurityExpressionOperations {

    private Object filterObject;
    private Object returnObject;
    private Object target;

    private final RoleMapping roleMapping;

    public CustomMethodSecurityExpressionRoot(Authentication authentication, RoleMapping roleMapping) {
        super(authentication);
        this.roleMapping = roleMapping;
    }

    public boolean fileRole(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String path = uri.substring(1).replace("/", ".");
        log.info("Path: {}", path);

        String requiredRole = roleMapping.getRoleForUri(path);
        if (requiredRole == null) {
            return false;
        }
        return hasAnyAuthority(requiredRole, "ROLE_ADMIN");

    }

    @Override
    public void setFilterObject(Object filterObject) {
        this.filterObject = filterObject;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public void setReturnObject(Object returnObject) {
        this.returnObject = returnObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return target;
    }

    public void setThis(Object target) {
        this.target = target;
    }
}