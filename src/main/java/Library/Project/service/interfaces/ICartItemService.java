package Library.Project.service.interfaces;

import Library.Project.dto.Response.CartResponse.CartItemResponse;
import Library.Project.dto.Response.CartResponse.UserCartResponse;
import Library.Project.entity.CartItem;

public interface ICartItemService {
    CartItemResponse addItemToCart(Long cartId, Long bookId, int quantity);

    CartItemResponse removeItemFromCart(Long cartId, Long cartItemId);

    CartItemResponse updateItemQuantity(Long cartId, Long bookId, int quantity);

    UserCartResponse cartResponse(Long userId);

    CartItem getCartItem(Long cartId, Long bookId);
}
