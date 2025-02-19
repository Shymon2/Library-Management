package Library.Project.Service.CartService;

import Library.Project.Model.Cart;
import Library.Project.Model.User;

public interface ICartService {
    Cart getCartById(Long id);

    Cart initializeNewCart();

    Cart getCartByUser(User user);

    void clearCartItem(Cart cart);
}
