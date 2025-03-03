package Library.Project.dto.Response;

import Library.Project.entity.Book;
import Library.Project.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private int quantity;

    private Book book;

    private UserInforResponse user;
}
