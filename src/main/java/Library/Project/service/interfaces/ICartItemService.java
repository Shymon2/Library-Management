package Library.Project.service.interfaces;

import Library.Project.entity.CartItem;

public interface ICartItemService {
    CartItem addItemToCart(Long cartId, Long bookId, int quantity);

    void removeItemFromCart(Long cartId, Long cartItemId);

    CartItem updateItemQuantity(Long cartId, Long bookId, int quantity);

    CartItem getCartItem(Long cartId, Long bookId);
}
