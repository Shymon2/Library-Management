package Library.Project.Service.CartService;

import Library.Project.Exception.BookQuantityExceedException;
import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Model.Book;
import Library.Project.Model.Cart;
import Library.Project.Model.CartItem;
import Library.Project.Repository.CartItemRepository;
import Library.Project.Service.BookService.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final CartService cartService;
    private final BookService bookService;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItem addItemToCart(Long cartId, Long bookId, int quantity) {
        //If book already in cart, then plus quantity
        if(cartItemRepository.existsByCartIdAndBookId(cartId, bookId)){
            CartItem cartItem = getCartItem(cartId, bookId);
            int newQuantity = cartItem.getQuantity() + quantity;
            if(newQuantity > bookService.findBookById(bookId).getQuantity())
                throw new BookQuantityExceedException("Borrow too much book");
            else{
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
            return cartItem;
        }
        //else create new cart item
        else {
            Cart cart = cartService.getCartById(cartId);
            Book book = bookService.findBookById(bookId);
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .build();
            if (quantity > book.getQuantity()) {
                throw new BookQuantityExceedException("Borrow too much book");
            } else {
                cartItem.setBook(book);
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
                cartService.addCartItem(cartItem, cart);
            }
            return cartItem;
        }
    }

    @Override
    public void removeItemFromCart(Long cartId, Long bookId) {
        CartItem cartItem = cartItemRepository.findByCartIdAndBookId(cartId, bookId);
        if(cartItem == null)
            throw new ResourcesNotFoundException("Item not found");

        Cart cart = cartService.getCartById(cartId);

        cartService.removeCartItem(cartItem, cart);

        cartItemRepository.delete(cartItem);
    }

    @Override
    public CartItem getCartItem(Long cartId, Long bookId){
        CartItem item = cartItemRepository.findByCartIdAndBookId(cartId, bookId);
        if(item == null)
            throw new ResourcesNotFoundException("Item not found");
        return item;
    }

    @Override
    public CartItem updateItemQuantity(Long cartId, Long bookId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        Book book = bookService.findBookById(bookId);
        if(quantity > book.getQuantity()){
            throw new BookQuantityExceedException("Borrow too much book");
        }
        else {
            CartItem cartItem = getCartItem(cartId, bookId);
            cartItem.setQuantity(quantity);
            cartService.updateCartItem(cartItem, cart);
            return cartItemRepository.save(cartItem);
        }
    }
}
