package Library.Project.controller;

import Library.Project.dto.GeneralPayload;
import Library.Project.dto.response.CartResponse.CartItemResponse;
import Library.Project.dto.response.CartResponse.UserCartResponse;
import Library.Project.service.RestfulResponseFactory;
import Library.Project.service.interfaces.ICartItemService;
import Library.Project.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
@RequestMapping("/${base.app.context}/api/v1/cartItem")
public class CartController {
    private final ICartItemService cartItemService;

    @Operation(summary = "Add item to cart")
    @PostMapping("/add-item-to-cart")
    public ResponseEntity<GeneralPayload<CartItemResponse>> addItemToCart(@RequestParam Long bookId,
                                                                         @RequestParam int quantity){
        return RestfulResponseFactory.of(cartItemService.addItemToCart(bookId, quantity));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Find cart by user Id", description = "Show cart of a user with Id (must be a positive number)")
    @GetMapping("/get-user-by-id")
    public ResponseEntity<GeneralPayload<UserCartResponse>> getUserById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id){
        return RestfulResponseFactory.of(cartItemService.cartResponse(id));
    }

    @Operation(summary = "Delete cart item ")
    @DeleteMapping("/delete")
    public ResponseEntity<GeneralPayload<CartItemResponse>> deleteItemFromCart(@RequestParam @Min(0) Long bookId){
        return RestfulResponseFactory.of(cartItemService.removeItemFromCart(bookId));
    }

    @Operation(summary = "Update quantity of item in cart", description = "Assignment quantity to quantity of item in cart")
    @PutMapping("/update-quantity")
    public ResponseEntity<GeneralPayload<CartItemResponse>> updateQuantity(@RequestParam @Min(0) Long bookId,
                                                     @RequestParam @Min(0) int quantity){
        return RestfulResponseFactory.of(cartItemService.updateItemQuantity(bookId, quantity));
    }
}
