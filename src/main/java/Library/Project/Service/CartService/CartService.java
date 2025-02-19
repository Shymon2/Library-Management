package Library.Project.Service.CartService;

import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Model.Cart;
import Library.Project.Model.CartItem;
import Library.Project.Model.User;
import Library.Project.Repository.CartRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartService implements ICartService{
    private final CartRepository cartRepository;

    public void addCartItem(CartItem request,Cart cart){
        cart.addCartItem(request);
        cartRepository.save(cart);
    }

    public void removeCartItem(CartItem request, Cart cart){
        cart.removeCartItem(request);
    }

    public void updateCartItem(CartItem request, Cart cart){
        cart.getCartItems().forEach(a -> {
            if(a.getId().equals(request.getId())){
                a.setQuantity(request.getQuantity());
            }
        });
        cartRepository.save(cart);
    }

    @Override
    public Cart getCartById(Long id) {
        return cartRepository.findById(id).orElseThrow(() ->
                new ResourcesNotFoundException("Cart with id " + id + " is not found"));
    }

    @Override
    public Cart initializeNewCart() {
        Cart cart = Cart.builder()
                .cartItems(new HashSet<>())
                .build();
        cartRepository.save(cart);
        log.info("new cart create");
        return cart;
    }

    @Override
    public Cart getCartByUser(User user) {
        return cartRepository.findByUser(user);
    }

    @Override
    public void clearCartItem(Cart cart) {
        cart.getCartItems().clear();
        cartRepository.save(cart);
    }
}
