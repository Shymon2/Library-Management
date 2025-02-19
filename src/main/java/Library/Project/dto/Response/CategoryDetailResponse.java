package Library.Project.dto.Response;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Builder
@Getter
public class CategoryDetailResponse implements Serializable {
    private String categoryName;
}
