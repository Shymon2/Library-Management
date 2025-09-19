package Library.Project.repository;

import Library.Project.entity.Category;
import Library.Project.dto.request.library.BooksByCateProjection;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    boolean existsByCategoryName(@NotBlank(message = "category must not be blank") String categoryName);

    Category findByCategoryName(@NotBlank(message = "category must not be blank") String categoryName);

    @Query(value = "SELECT c.category_name AS category, COUNT(bc.book_id) AS quantity " +
            "FROM category c " +
            "JOIN book_category bc ON c.id = bc.category_id " +
            "GROUP BY c.category_name",
            nativeQuery = true)
    List<BooksByCateProjection> findNumBookByCategoryName();
}
