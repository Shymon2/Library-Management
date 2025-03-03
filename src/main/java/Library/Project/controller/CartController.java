package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.entity.CartItem;
import Library.Project.entity.User;
import Library.Project.service.implement.CartItemService;
import Library.Project.service.implement.UserService;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.UserCartResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
    private final CartItemService cartItemService;
    private final UserService userService;

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Add item to cart")
    @PostMapping("/add-item-to-cart")
    public ResponseData<CartItem> addItemToCart(@RequestParam Long bookId,
                                                @RequestParam int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItem cartItem = cartItemService.addItemToCart(userFound.getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.add.item.success"), cartItem);
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.cart.getCartById'))")
    @Operation(summary = "Find cart by user Id", description = "Show cart of a user with Id (must be a positive number)")
    @GetMapping("/get-user-by-id")
    public ResponseData<UserCartResponse> getUserById(@RequestParam @Min(0) Long id){
        User userFound = userService.getUserById(id);
        UserCartResponse userCartResponseResponse = userService.convertToCartResponse(userFound);
        return new ResponseData<>(1000, Translator.toLocale("cart.get.cart.id"), userCartResponseResponse);
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Delete cart item ")
    @DeleteMapping("/delete")
    public ResponseData<String> deleteItemFromCart(@RequestParam @Min(0) Long bookId){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        cartItemService.removeItemFromCart(userFound.getId(), bookId);
        return new ResponseData<>(1000, Translator.toLocale("cart.delete.item"));
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Update quantity of item in cart", description = "Assignment quantity to quantity of item in cart")
    @PutMapping("/update-quantity")
    public ResponseData<CartItem> updateQuantity(@RequestParam @Min(0) Long bookId,
                                                 @RequestParam @Min(0) int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItem cartItem = cartItemService.updateItemQuantity(userFound.getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.update.quantity"), cartItem);
    }

    @PostAuthorize("returnObject.data.username == authentication.name")
    @Operation(summary = "Get cart items of user")
    @GetMapping("/current-cart")
    public ResponseData<UserCartResponse> getCurrentUser(Authentication authentication) {
        User userFound = userService.findUserByUserName(authentication.getName());
        return new ResponseData<>(1000, Translator.toLocale("cart.show.user"), userService.convertToCartResponse(userFound));
    }
}
