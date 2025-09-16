package Library.Project.dto.Response.CartResponse;

import Library.Project.entity.Book;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CartItemResponse {
    private Book book;

    private int quantity;

    @JsonIgnore
    private String username;
}
