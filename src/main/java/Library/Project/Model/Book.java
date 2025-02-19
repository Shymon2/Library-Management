package Library.Project.Model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.*;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book extends AbstractEntity{

    @Column(name = "title")
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "quantity")
    private int quantity;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "book_category",
            joinColumns = @JoinColumn(name = "book_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();

    public void saveCategory(Category category) {
        if (category != null && !categories.contains(category)) {  // Prevent duplicate addition
            categories.add(category);
            category.saveBook(this);  // Only call when the category isn't already associated with this book
        }
    }

}
