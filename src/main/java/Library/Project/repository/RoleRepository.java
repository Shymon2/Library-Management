package Library.Project.repository;

import Library.Project.entity.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    boolean existsByName(String name);

    Role findByName(String name);

    Page<Role> findByIsDelete(PageRequest of, boolean b);
}
