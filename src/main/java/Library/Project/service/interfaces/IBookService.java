package Library.Project.service.interfaces;

import Library.Project.dto.Request.Library.BookSearchRequest;
import Library.Project.dto.Response.LibraryResponse.BookDetailResponse;
import Library.Project.entity.Book;
import Library.Project.dto.Request.Library.BookRequestDTO;
import Library.Project.dto.Response.ApiResponse.PageResponse;

public interface IBookService {
    Book findBookById(Long id);

    Book addNewBook(BookRequestDTO request);

    PageResponse findBooksByCriteria(BookSearchRequest request, int pageNo, int pageSize);

    PageResponse findALlBook(int pageNo, int pageSize);

    void deleteBookById(Long id);

    Book updateBook(BookRequestDTO request, Long id);

    void updateBookQuantity(Long bookId, int quantity);

    Boolean existsByTitleAndAuthor(String title, String author);

    BookDetailResponse convertToResponse(Book book);
}
