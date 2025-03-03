package Library.Project.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Set;

@Builder
@Getter
public class BookDetailResponse implements Serializable {
    private String title;
    private String author;
    private int quantity;
    private Set<String> categories;
}
