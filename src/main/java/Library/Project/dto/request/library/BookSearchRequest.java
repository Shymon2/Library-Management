package Library.Project.dto.request.library;

import lombok.Getter;

import java.util.List;

@Getter
public class BookSearchRequest {
    private String title;
    private String author;
    private List<String> category;
}
