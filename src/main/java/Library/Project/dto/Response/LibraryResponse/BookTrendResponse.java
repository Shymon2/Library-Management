package Library.Project.dto.Response.LibraryResponse;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class BookTrendResponse {
    private String title;
    private String author;
    private int orderQuantity;
}
