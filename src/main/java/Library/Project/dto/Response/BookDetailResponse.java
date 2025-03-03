package Library.Project.dto.Response;

import Library.Project.dto.Request.CategoryDTO;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Builder
@Getter
public class BookDetailResponse implements Serializable {
    private String title;
    private String author;
    private int quantity;
    private Set<String> categories;
}
