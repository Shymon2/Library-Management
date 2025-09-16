package Library.Project.dto.Response.LibraryResponse;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class CategoryDetailResponse implements Serializable {
    private String categoryName;
}
