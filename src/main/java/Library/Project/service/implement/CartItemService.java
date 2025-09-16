package Library.Project.service.implement;

import Library.Project.dto.Response.CartResponse.CartItemResponse;
import Library.Project.dto.Response.CartResponse.UserCartResponse;
import Library.Project.entity.User;
import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import Library.Project.entity.Book;
import Library.Project.entity.CartItem;
import Library.Project.repository.CartItemRepository;
import Library.Project.service.interfaces.ICartItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CartItemService implements ICartItemService {

    private final UserService userService;
    private final BookService bookService;
    private final CartItemRepository cartItemRepository;

    @Override
    public CartItemResponse addItemToCart(Long userId, Long bookId, int quantity) {
        //If book already in cart, then plus quantity
        if(cartItemRepository.existsByUserIdAndBookId(userId, bookId)){
            CartItem cartItem = getCartItem(userId, bookId);
            int newQuantity = cartItem.getQuantity() + quantity;
            if(newQuantity > bookService.findBookById(bookId).getQuantity())
                throw new AppException(ErrorCode.BOOK_QUANTITY_EXCEED);
            else{
                cartItem.setQuantity(newQuantity);
                cartItemRepository.save(cartItem);
            }
            return CartItemResponse.builder()
                    .book(cartItem.getBook())
                    .quantity(cartItem.getQuantity())
                    .username(cartItem.getUser().getUsername())
                    .build();
        }
        //else create new cart item
        else {
            User user = userService.getUserById(userId);
            Book book = bookService.findBookById(bookId);
            CartItem cartItem = CartItem.builder()
                    .user(user)
                    .build();
            if (quantity > book.getQuantity()) {
                throw new AppException(ErrorCode.BOOK_QUANTITY_EXCEED);
            } else {
                cartItem.setBook(book);
                cartItem.setQuantity(quantity);
                cartItemRepository.save(cartItem);
            }
            return CartItemResponse.builder()
                    .book(cartItem.getBook())
                    .quantity(cartItem.getQuantity())
                    .username(cartItem.getUser().getUsername())
                    .build();
        }
    }

    @Override
    public CartItemResponse removeItemFromCart(Long userId, Long bookId) {
        CartItem cartItem = cartItemRepository.findByUserIdAndBookId(userId, bookId);
        if(cartItem == null)
            throw new AppException(ErrorCode.NOT_FOUND);
        cartItem.setDelete(true);
        return CartItemResponse.builder()
                .book(cartItem.getBook())
                .quantity(cartItem.getQuantity())
                .username(cartItem.getUser().getUsername())
                .build();
    }

    @Override
    public CartItem getCartItem(Long userId, Long bookId){
        CartItem item = cartItemRepository.findByUserIdAndBookId(userId, bookId);
        if(item == null)
            throw new AppException(ErrorCode.NOT_FOUND);
        return item;
    }

    @Override
    public CartItemResponse updateItemQuantity(Long userId, Long bookId, int quantity) {
        User user = userService.getUserById(userId);
        Book book = bookService.findBookById(bookId);
        if(quantity > book.getQuantity()){
            throw new AppException(ErrorCode.BOOK_QUANTITY_EXCEED);
        }
        else {
            CartItem cartItem = getCartItem(userId, bookId);
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
            return CartItemResponse.builder()
                    .book(cartItem.getBook())
                    .quantity(cartItem.getQuantity())
                    .username(cartItem.getUser().getUsername())
                    .build();
        }
    }

    @Override
    public UserCartResponse cartResponse(Long userId){
            User user = userService.getUserById(userId);
            List<CartItem> cartItems = getItemsByUser(user.getId());
            List<CartItemResponse> list = new ArrayList<>();
            cartItems.forEach(a -> list.add(CartItemResponse.builder()
                    .book(a.getBook())
                    .quantity(a.getQuantity())
                    .build()));
            return UserCartResponse.builder()
                    .username(user.getUsername())
                    .fullname(user.getFullname())
                    .identityNum(user.getIdentityNum())
                    .phoneNumber(user.getPhoneNumber())
                    .dateOfBirth(user.getDateOfBirth())
                    .address(user.getAddress())
                    .listItems(list)
                    .build();
    }

    public List<CartItem> getItemsByUser(Long userId){
        return cartItemRepository.findByUserId(userId);
    }
}
