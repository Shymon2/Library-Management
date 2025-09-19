package Library.Project.dto.response.LibraryResponse;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class BookTrendResponse {
    private String title;
    private String author;
    private int orderQuantity;
}
