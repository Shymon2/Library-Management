package Library.Project.dto.Response.OrderResponse;

import Library.Project.enums.OrderStatus;
import Library.Project.entity.OrderItem;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Builder
public class OrderInforResponse {
    private Long id;

    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private Set<OrderItem> orderItems;
}
