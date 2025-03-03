package Library.Project.specification;

import Library.Project.dto.Request.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.entity.Category;
import org.springframework.data.jpa.domain.Specification;
import jakarta.persistence.criteria.*;

public class BookSpecification {

    public static Specification<Book> filterBooks(BookSearchRequest request) {
        return (Root<Book> bookScheme, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (request.getTitle() != null) {
                predicate = cb.and(predicate, cb.like(bookScheme.get("title"), "%" + request.getTitle() + "%"));
            }
            if (request.getAuthor() != null) {
                predicate = cb.and(predicate, cb.like(bookScheme.get("author"), "%" + request.getAuthor() + "%"));
            }
            if (request.getCategory() != null && !request.getCategory().isEmpty()) {
                Join<Book, Category> categoryJoin = bookScheme.join("categories", JoinType.INNER);
                CriteriaBuilder.In<String> inClause = cb.in(categoryJoin.get("categoryName"));

                for (String category : request.getCategory()) {
                    inClause.value(category);
                }

                predicate = cb.and(predicate, inClause);
            }

            return predicate;
        };
    }

}