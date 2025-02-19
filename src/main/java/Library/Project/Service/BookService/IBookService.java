package Library.Project.Service.BookService;

import Library.Project.Model.Book;
import Library.Project.dto.Request.BookRequestDTO;
import Library.Project.dto.Response.BookDetailResponse;
import Library.Project.dto.Response.PageResponse;

import java.util.List;

public interface IBookService {
    Book findBookById(Long id);

    Book addNewBook(BookRequestDTO request);

    List<BookDetailResponse> findBookByTitle(String title);

    List<BookDetailResponse> findBookByAuthor(String author);

    List<BookDetailResponse> findBookByTitleAndAuthor(String Title, String Author);

    PageResponse findALlBook(int pageNo, int pageSize);

    Book deleteBookById(Long id);

    Book updateBook(BookRequestDTO request, Long id);

    void updateBookQuantity(Long bookId, int quantity);

    Boolean existsByTitleAndAuthor(String title, String author);
}
