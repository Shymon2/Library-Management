package Library.Project.dto.response.LibraryResponse;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CategoryStaticResponse {
    private String category;
    private int quantity;
}
