package Library.Project.Repository;

import Library.Project.Model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByCartIdAndBookId(Long cart_id, Long book_id);

    boolean existsByCartIdAndBookId(Long cart_id, Long book_id);
}
