package Library.Project.dto.Response;

import Library.Project.Enums.OrderStatus;
import Library.Project.Model.OrderItem;
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
