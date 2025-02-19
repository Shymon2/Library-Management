package Library.Project.Configuration;

import Library.Project.Enums.Role;
import Library.Project.Model.User;
import Library.Project.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.experimental.NonFinal;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@RequiredArgsConstructor
public class ApplicationInitConfig {

    @NonFinal
    private static final String ADMIN_USER_NAME = "admin";

    @NonFinal
    private static final String ADMIN_PASSWORD = "admin";

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository){
        return args -> {
            PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

            if(!userRepository.existsByUsername("admin")){

                    User user = User.builder()
                            .username(ADMIN_USER_NAME)
                            .password(passwordEncoder.encode(ADMIN_PASSWORD))
                            .role(Role.ADMIN)
                            .build();

                    userRepository.save(user);
            }
        };
    }
}
