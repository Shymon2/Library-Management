package Library.Project.repository;

import Library.Project.entity.Permission;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    boolean existsByName(String name);

    Page<Permission> findPermissionByIsDelete(PageRequest of, boolean b);

    Permission findByName(String name);
}
