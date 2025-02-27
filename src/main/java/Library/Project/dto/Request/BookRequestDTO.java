package Library.Project.dto.Request;

import Library.Project.Model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
public class BookRequestDTO implements Serializable {
    @NotBlank(message = "title must be not blank")
    private String title;

    @NotBlank(message = "title must be not blank")
    private String author;

    @Min(0)
    private int quantity;

    @NotEmpty(message = "category must have at least 1")
    private Set<CategoryDTO> categories;

}
