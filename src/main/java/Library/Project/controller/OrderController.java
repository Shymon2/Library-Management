package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.enums.OrderStatus;
import Library.Project.entity.Orders;
import Library.Project.entity.User;
import Library.Project.service.implement.OrderService;
import Library.Project.service.implement.UserService;
import Library.Project.dto.Response.OrderInforResponse;
import Library.Project.dto.Response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/order")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Order Controller")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Place order")
    @PostMapping("/place-order/user")
    public ResponseData<Orders> placeOrderByUserId(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        Orders order = orderService.createOrder(userFound.getId());
        return new ResponseData<>(1000, Translator.toLocale("order.place.success"), order);
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.approveOrder'))")
    @Operation(summary = "Approve order by manager", description = "Approve by order Id (must be positive number)")
    @PatchMapping("/approve/order")
    public ResponseData<String> approveOrderById(@RequestParam @Min(0) Long orderId){
        orderService.approveOrder(orderId);
        return new ResponseData<>(1000, Translator.toLocale("order.approve.success"));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.setBorrowed'))")
    @Operation(summary = "Set borrowing order by manager", description = "Set borrowing by order Id (must be positive number)")
    @PatchMapping("/borrowed/order")
    public ResponseData<String> setBorrowed(@RequestParam @Min(0) Long orderId){
        orderService.borrowBook(orderId);
        return new ResponseData<>(1000, Translator.toLocale("order.set.borrowed"));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.setReturn'))")
    @Operation(summary = "Return book by order Id", description = "Return book by order Id (must be positive number), then plus book quantity")
    @PatchMapping("/return-book/order")
    public ResponseData<String> returnBook(@RequestParam @Min(0) Long orderId){
        orderService.returnBook(orderId);
        return new ResponseData<>(1000, Translator.toLocale("order.set.return"));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.setOverDue'))")
    @Operation(summary = "Set over due date of borrowing book")
    @PatchMapping("/set-over-due")
    public ResponseData<String> setOverDue(){
        orderService.setOverDate();
        return new ResponseData<>(1000, Translator.toLocale("order.set.overdue"));
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name || hasRole('ADMIN') || hasRole('MANAGER')")
    @Operation(summary = "Cancel order by order Id", description = "Id must be positive")
    @PatchMapping("/cancel-order")
    public ResponseData<String> cancelOrderById(@RequestParam @Min(0) Long orderId){
        orderService.cancelOrder(orderId);
        return new ResponseData<>(1000, Translator.toLocale("order.cancel.success"));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.getOrderByUser'))")
    @Operation(summary = "Get all order of user by user Id", description = "Id must be positive")
    @PatchMapping("/user-order")
    public ResponseData<List<Orders>> getUserOrder(@RequestParam @Min(0) Long userId){
        return new ResponseData<>(1000, Translator.toLocale("order.get.userid"), orderService.getUserOrder(userId));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.order.getOrderByStatus'))")
    @Operation(summary = "Get all order by order status", description = "Id must be positive")
    @PatchMapping("/orders-by-status")
    public ResponseData<List<Orders>> getOrderByStatus(@RequestParam OrderStatus status){
        return new ResponseData<>(1000, Translator.toLocale("order.get.status"), orderService.getAllOrderByStatus(status));
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Get current user order items")
    @GetMapping("/current-order")
    public ResponseData<List<OrderInforResponse>> currentOrder(){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        List<Orders> order = orderService.getUserOrder(userFound.getId());
        List<OrderInforResponse> orderInforResponseList = new ArrayList<>();
        order.forEach(a -> orderInforResponseList.add(orderService.convertToInforResponse(a)));

        return new ResponseData<>(1000, Translator.toLocale("order.get.current.user"), orderInforResponseList);
    }
}
