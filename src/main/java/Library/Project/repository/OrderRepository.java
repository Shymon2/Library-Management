package Library.Project.repository;

import Library.Project.enums.OrderStatus;
import Library.Project.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, Long> {
    List<Orders> findByStatusAndDueDateBefore(OrderStatus orderStatus, LocalDate now);

    List<Orders> findByUserId(Long user_id);

    List<Orders> findByStatus(OrderStatus status);
}
