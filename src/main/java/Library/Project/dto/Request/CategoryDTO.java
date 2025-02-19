package Library.Project.dto.Request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Builder
public class CategoryDTO implements Serializable {
    @NotBlank(message = "category must not be blank")
    private String categoryName;
}
