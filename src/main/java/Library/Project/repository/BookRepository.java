package Library.Project.repository;

import Library.Project.entity.Book;
import Library.Project.dto.Request.BookTrendDTO;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
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
    List<BookTrendDTO> findTop5BookOrder();

    Page<Book> findBookByIsDelete(PageRequest of, boolean b);
}
