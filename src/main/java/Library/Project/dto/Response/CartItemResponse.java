package Library.Project.dto.Response;

import Library.Project.entity.Book;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private int quantity;

    private Book book;

    private UserInforResponse user;
}
