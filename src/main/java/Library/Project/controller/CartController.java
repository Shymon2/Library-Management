package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.Response.CartResponse.CartItemResponse;
import Library.Project.entity.User;
import Library.Project.dto.Response.CartResponse.UserCartResponse;
import Library.Project.service.interfaces.ICartItemService;
import Library.Project.service.interfaces.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
@RequestMapping("/cartItem")
public class CartController {
    private final ICartItemService cartItemService;
    private final IUserService userService;

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Add item to cart")
    @PostMapping("/add-item-to-cart")
    public ResponseData<CartItemResponse> addItemToCart(@RequestParam Long bookId,
                                                @RequestParam int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItemResponse cartItemResponse = cartItemService.addItemToCart(userFound.getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.add.item.success"), cartItemResponse);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Find cart by user Id", description = "Show cart of a user with Id (must be a positive number)")
    @GetMapping("/get-user-by-id")
    public ResponseData<UserCartResponse> getUserById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id){
        return new ResponseData<>(1000, Translator.toLocale("cart.get.cart.id"), cartItemService.cartResponse(id));
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Delete cart item ")
    @DeleteMapping("/delete")
    public ResponseData<CartItemResponse> deleteItemFromCart(@RequestParam @Min(0) Long bookId){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());
        return new ResponseData<>(1000, Translator.toLocale("cart.delete.item"),
                cartItemService.removeItemFromCart(userFound.getId(), bookId));
    }

    @PostAuthorize(value = "returnObject.data.user.username == authentication.name")
    @Operation(summary = "Update quantity of item in cart", description = "Assignment quantity to quantity of item in cart")
    @PutMapping("/update-quantity")
    public ResponseData<CartItemResponse> updateQuantity(@RequestParam @Min(0) Long bookId,
                                                 @RequestParam @Min(0) int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItemResponse cartItemResponse = cartItemService.updateItemQuantity(userFound.getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.update.quantity"), cartItemResponse);
    }

    @PostAuthorize("returnObject.data.username == authentication.name")
    @Operation(summary = "Get cart items of user")
    @GetMapping("/current-cart")
    public ResponseData<UserCartResponse> getCurrentUser(Authentication authentication) {
        User userFound = userService.findUserByUserName(authentication.getName());
        return new ResponseData<>(1000, Translator.toLocale("cart.show.user"), cartItemService.cartResponse(userFound.getId()));
    }
}
