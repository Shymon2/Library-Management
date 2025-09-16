package Library.Project.repository;

import Library.Project.dto.Request.Library.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.dto.Request.Library.BookTrendProjection;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface BookRepository extends JpaRepository<Book, Long>, JpaSpecificationExecutor<Book> {


    boolean existsByTitleAndAuthor(@NotBlank(message = "title must be not blank") String title, @NotBlank(message = "title must be not blank") String author);

    Book findBookByTitleAndAuthor(@NotBlank(message = "title must be not blank") String title, @NotBlank(message = "title must be not blank") String author);

    @Query(value = "select b.title as Title, b.author as Author, count(oi.quantity) as order_quantity " +
            "from book as b join order_item as oi on b.id = oi.book_id " +
            "group by b.title, b.author " +
            "order by order_quantity desc limit 5", nativeQuery = true)
    List<BookTrendProjection> findTop5BookOrder();

    Page<Book> findBookByIsDelete(PageRequest of, boolean b);

    @Query("SELECT DISTINCT b FROM Book b JOIN b.categories c " +
            "WHERE b.isDelete = false " +
            "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%'))) " +
            "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
            "AND (:#{#categories == null or #categories.isEmpty()} = true OR c.categoryName IN :categories)")
    Page<Book> search(
            @Param("title") String title,
            @Param("author") String author,
            @Param("categories") List<String> categories,
            Pageable pageable
    );

}
