package Library.Project.repository;

import Library.Project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    CartItem findByUserIdAndBookId(Long user_id, Long book_id);

    boolean existsByUserIdAndBookId(Long cart_id, Long book_id);

    List<CartItem> findByUserId(Long userId);
}
