package Library.Project.configuration;

import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.core.Authentication;

import java.io.IOException;

@Slf4j
public class CustomMethodSecurityExpressionHandler extends DefaultMethodSecurityExpressionHandler {
    @Autowired
    private Environment env;

    @Override
    protected MethodSecurityExpressionOperations createSecurityExpressionRoot(Authentication authentication, MethodInvocation invocation) {
        try {
            RoleMapping roleMapping = new RoleMapping(env);
            CustomMethodSecurityExpressionRoot root = new CustomMethodSecurityExpressionRoot(authentication, roleMapping);
            root.setThis(invocation.getThis());
            root.setPermissionEvaluator(getPermissionEvaluator());
            root.setTrustResolver(getTrustResolver());
            root.setRoleHierarchy(getRoleHierarchy());
            return root;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
