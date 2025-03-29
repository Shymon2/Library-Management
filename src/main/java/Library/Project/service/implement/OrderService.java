package Library.Project.service.implement;

import Library.Project.configuration.Translator;
import Library.Project.entity.CartItem;
import Library.Project.enums.ErrorCode;
import Library.Project.enums.OrderStatus;
import Library.Project.entity.Orders;
import Library.Project.entity.OrderItem;
import Library.Project.entity.User;
import Library.Project.exception.AppException;
import Library.Project.repository.OrderRepository;
import Library.Project.dto.Response.OrderResponse.OrderInforResponse;
import Library.Project.service.interfaces.IOrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final CartItemService cartItemService;
    private final UserService userService;
    private final BookService bookService;

    @Override
    public Orders createOrder(Long userID) {
        //get cart by user Id
        User user = userService.getUserById(userID);
        List<CartItem> itemList = cartItemService.getItemsByUser(userID);

        //check if cart is empty, then stop
        if(itemList.isEmpty()){
            throw new RuntimeException(Translator.toLocale("order.cart.empty"));
        }

        Orders newOrder = new Orders();

        //move cart item into order item
        Set<OrderItem> orderItem = new HashSet<>();
        itemList.forEach(a -> {
                    orderItem.add(OrderItem.builder()
                                    .quantity(a.getQuantity())
                                    .book(a.getBook())
                                    .order(newOrder)
                                    .build());
        });

        //set new oder
        newOrder.setUser(user);
        newOrder.setOrderItems(orderItem);
        newOrder.setStatus(OrderStatus.PENDING);
        newOrder.setDueDate(LocalDate.now().plusWeeks(2));

        //remove cart item out of cart_item db, then minus book quantity
        orderItem.forEach(a -> {
            cartItemService.removeItemFromCart(userID, a.getBook().getId());
            int newQuantity = a.getBook().getQuantity() - a.getQuantity();
            bookService.updateBookQuantity(a.getBook().getId(), newQuantity);
        });
        return orderRepository.save(newOrder);
    }

    @Override
    public void approveOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(Translator.toLocale("order.only.pending"));
        }
        order.setStatus(OrderStatus.APPROVED);
        orderRepository.save(order);
    }

    @Override
    public void borrowBook(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        if (order.getStatus() != OrderStatus.APPROVED) {
            throw new IllegalStateException(Translator.toLocale("order.only.approve"));
        }
        order.setStatus(OrderStatus.BORROWED);
        orderRepository.save(order);
    }

    @Override
    public void returnBook(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        if (order.getStatus() == OrderStatus.BORROWED) {
            order.setStatus(OrderStatus.RETURNED);

            order.getOrderItems().forEach(a -> {
                int bookQuantity = a.getBook().getQuantity() + a.getQuantity();
                bookService.updateBookQuantity(a.getBook().getId(), bookQuantity);
            });

            orderRepository.save(order);
        }
        else
            throw new IllegalStateException(Translator.toLocale("order.only.borrowed"));
    }

    @Override
    public void setOverDate() {
        List<Orders> overdueOrders = orderRepository.findByStatusAndDueDateBefore(OrderStatus.BORROWED, LocalDate.now());

        overdueOrders.forEach(a -> {
            a.setStatus(OrderStatus.OVERDUE);
            orderRepository.save(a);
        });
    }

    @Override
    public void cancelOrder(Long orderId) {
        Orders order = orderRepository.findById(orderId).orElseThrow(() ->
                new AppException(ErrorCode.NOT_FOUND));
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException(Translator.toLocale("order.only.pending"));
        }
        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    @Override
    public List<Orders> getUserOrder(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public List<Orders> getAllOrderByStatus(OrderStatus status) {
        if (status != null) {
            return orderRepository.findByStatus(status);
        }
        else throw new RuntimeException(Translator.toLocale("order.status.fail"));
    }

    public OrderInforResponse convertToInforResponse(Orders order){
        return OrderInforResponse.builder()
                .id(order.getId())
                .dueDate(order.getDueDate())
                .status(order.getStatus())
                .orderItems(order.getOrderItems())
                .build();
    }
}
