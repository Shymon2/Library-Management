package Library.Project.configuration;

import Library.Project.entity.User;
import Library.Project.repository.UserRepository;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.client.RestTemplateCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;


@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    @NonFinal
    private static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    private static final String ADMIN_PASSWORD = "admin";

    @Bean
    public RestTemplateCustomizer restTemplateCustomizer() {
        return restTemplateCustomizer -> {
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setConnectTimeout(5000); // Timeout kết nối: 5 giây
            factory.setReadTimeout(10000);   // Timeout đọc: 10 giây
            restTemplateCustomizer.setRequestFactory(factory);

            // Thêm header mặc định (ví dụ)
            restTemplateCustomizer.getInterceptors().add((request, body, execution) -> {
                request.getHeaders().add("Authorization", "Bearer token123");
                return execution.execute(request, body);
            });
        };
    }

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        return builder
                .additionalCustomizers(restTemplateCustomizer())
                .build();
    }

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

            if(!userRepository.existsByUsername("admin")){

                    User user = User.builder()
                            .username(ADMIN_USER_NAME)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .role("ADMIN")
                            .build();

                    userRepository.save(user);
            }
        };
    }
}
