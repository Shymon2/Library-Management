package Library.Project.service.interfaces;

import Library.Project.dto.response.CartResponse.CartItemResponse;
import Library.Project.dto.response.CartResponse.UserCartResponse;
import Library.Project.entity.CartItem;

public interface ICartItemService {
    CartItemResponse addItemToCart(Long bookId, int quantity);

    CartItemResponse removeItemFromCart(Long cartItemId);

    CartItemResponse updateItemQuantity(Long bookId, int quantity);

    UserCartResponse cartResponse(Long userId);

    CartItem getCartItem(Long cartId, Long bookId);
}
