package Library.Project.service.interfaces;

import Library.Project.dto.request.library.BookSearchRequest;
import Library.Project.dto.response.LibraryResponse.BookDetailResponse;
import Library.Project.entity.Book;
import Library.Project.dto.request.library.BookRequestDTO;
import Library.Project.dto.response.ApiResponse.PageResponse;

import java.util.List;

public interface IBookService {
    List<BookDetailResponse> findBookByName(String name);

    Object addNewBook(BookRequestDTO request);

    PageResponse<List<BookDetailResponse>> findBooksByCriteria(BookSearchRequest request, int pageNo, int pageSize);

    PageResponse<List<BookDetailResponse>> findALlBook(int pageNo, int pageSize);

    Object deleteBookById(Long id);

    Object updateBook(BookRequestDTO request, Long id);

    void updateBookQuantity(Long bookId, int quantity);

    Boolean existsByTitleAndAuthor(String title, String author);
}
