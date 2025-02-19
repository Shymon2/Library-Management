package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Exception.BookQuantityExceedException;
import Library.Project.Model.CartItem;
import Library.Project.Model.User;
import Library.Project.Service.CartService.CartItemService;
import Library.Project.Service.CartService.CartService;
import Library.Project.Service.UserService.UserService;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.ResponseError;
import Library.Project.dto.Response.UserCartResponse;
import Library.Project.dto.Response.UserInforResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
    @PostMapping("/addItemToCart")
    public ResponseData<CartItem> addItemToCart(@RequestParam Long bookId,
                                                @RequestParam int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItem cartItem = cartItemService.addItemToCart(userFound.getCart().getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.add.item.success"), cartItem);
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.cart.getCartById'))")
    @Operation(summary = "Find cart by user Id", description = "Show cart of a user with Id (must be a positive number)")
    @GetMapping("/getUserById")
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

        cartItemService.removeItemFromCart(userFound.getCart().getId(), bookId);
        return new ResponseData<>(1000, Translator.toLocale("cart.delete.item"));
    }

    @PostAuthorize(value = "returnObject.data.username == authentication.name")
    @Operation(summary = "Update quantity of item in cart", description = "Assignment quantity to quantity of item in cart")
    @PutMapping("/updateQuantity")
    public ResponseData<CartItem> updateQuantity(@RequestParam @Min(0) Long bookId,
                                                 @RequestParam @Min(0) int quantity){
        Authentication user = SecurityContextHolder.getContext().getAuthentication();
        User userFound = userService.findUserByUserName(user.getName());

        CartItem cartItem = cartItemService.updateItemQuantity(userFound.getCart().getId(), bookId, quantity);
        return new ResponseData<>(1000, Translator.toLocale("cart.update.quantity"), cartItem);
    }

    @GetMapping("/currentCart")
    @PostAuthorize("returnObject.data.username == authentication.name")
    public ResponseData<UserCartResponse> getCurrentUser(Authentication authentication) {
        User userFound = userService.findUserByUserName(authentication.getName());
        return new ResponseData<>(1000, Translator.toLocale("cart.show.user"), userService.convertToCartResponse(userFound));
    }
}
