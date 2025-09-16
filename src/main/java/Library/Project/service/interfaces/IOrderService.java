package Library.Project.service.interfaces;

import Library.Project.dto.Response.OrderResponse.OrderInforResponse;
import Library.Project.enums.OrderStatus;
import Library.Project.entity.Orders;

import java.util.List;

public interface IOrderService {

    Orders createOrder(Long userId);

    void approveOrder(Long orderId);

    void borrowBook(Long orderId);

    void returnBook(Long orderId);

    void setOverDate();

    void cancelOrder(Long orderId);

    List<Orders> getUserOrder(Long userId);

    List<Orders> getAllOrderByStatus(OrderStatus status);

    OrderInforResponse convertToInforResponse(Orders order);
}
