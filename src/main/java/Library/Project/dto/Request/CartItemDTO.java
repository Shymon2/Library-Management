package Library.Project.dto.Request;

import Library.Project.Model.Book;
import Library.Project.Model.Cart;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemDTO {
    private Book book;
    private int quantity;
    private Cart cart;
}
