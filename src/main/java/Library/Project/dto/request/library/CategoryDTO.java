package Library.Project.dto.request.library;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Getter
@Builder
public class CategoryDTO implements Serializable {
    @NotBlank(message = "category must not be blank")
    private String categoryName;
}
