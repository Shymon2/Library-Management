package Library.Project.Model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category extends AbstractEntity{

    private String categoryName;

    @JsonIgnore
    @ManyToMany(mappedBy = "categories", cascade = CascadeType.PERSIST)
    private Set<Book> books = new HashSet<>();

    public void saveBook(Book book) {
        if (book != null && !books.contains(book)) {  // Prevent duplicate addition
            books.add(book);
            book.saveCategory(this);  // Only call when the book isn't already associated with this category
        }
    }

}
