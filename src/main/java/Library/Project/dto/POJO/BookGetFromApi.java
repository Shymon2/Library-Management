package Library.Project.dto.POJO;

import lombok.Data;

import java.util.List;

@Data
public class BookGetFromApi {
    private Long id;
    private String title;
    private List<String> authors;
    private String publisher;
    private int pageCount;
    private String language;
}
