package Library.Project.Repository;

import Library.Project.Model.User;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByUsername(@NotBlank String username);

    User findByUsername(String username);

    @Query(value = "select u.* " +
            "from user as u " +
            "where u.role = :role", nativeQuery = true)
    List<User> findUserByRole(@Param("role") String role);
}
