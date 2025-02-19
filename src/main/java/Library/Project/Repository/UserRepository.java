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
            "from user as u join user_role as ur on u.id = ur.user_id " +
            "where ur.role_name = :role", nativeQuery = true)
    List<User> findUseIdByRoles(@Param("role") String role);
}
