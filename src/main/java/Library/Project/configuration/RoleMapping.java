package Library.Project.configuration;

import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

@Component
public class RoleMapping {
    private final Map<String, String> roleMappings = new HashMap<>();

    public RoleMapping(Environment env) throws IOException {
        Resource resource = new ClassPathResource("role.properties");
        Properties properties = new Properties();
        properties.load(resource.getInputStream());

        Set<String> keys = properties.stringPropertyNames();
        for (String key : keys) {
            roleMappings.put(key, properties.getProperty(key));
        }
    }

    public String getRoleForApi(String api) {
        return roleMappings.getOrDefault(api, "ROLE_DEFAULT");
    }
}
