package Library.Project.Service.OrderService;

import Library.Project.Enums.OrderStatus;
import Library.Project.Model.Orders;

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

}
