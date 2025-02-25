package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Model.Book;
import Library.Project.Service.BookService.BookService;
import Library.Project.dto.Request.BookRequestDTO;
import Library.Project.dto.Response.BookDetailResponse;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.ResponseError;
import Library.Project.Service.BookService.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
@Validated
@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "Book Controller")
public class BookController {

    private final BookService bookService;

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.book.getBookById'))")
    @Operation(summary = "Find book by ID", description = "ID must be positive")
    @GetMapping("/findById")
    public ResponseData<Book> getBookById(@RequestParam @Min(0) Long id) {
        Book bookFound = bookService.findBookById(id);
        return new ResponseData<>(1000, Translator.toLocale("book.get.success"), bookFound);
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.book.addNewBook'))")
    @Operation(summary = "Add new Book")
    @SecurityRequirement(name = "BearerAuth")
    @PostMapping("/add")
    public ResponseData<Book> addNewBook(@Valid @RequestBody BookRequestDTO request) {

        if (bookService.existsByTitleAndAuthor(request.getTitle(), request.getAuthor())) {
            Book updateBook = bookService.addNewBook(request);
            return new ResponseData<>(1000, Translator.toLocale("book.update.instead"), updateBook);
        } else {
            Book newBook = bookService.addNewBook(request);
            return new ResponseData<>(1000, Translator.toLocale("book.add.success"), newBook);
        }

    }

    @Operation(summary = "Find book by title")
    @GetMapping("/findByTitle")
    public ResponseData<List<BookDetailResponse>> getBookByTitle(@RequestParam String title) {
        List<BookDetailResponse> bookFound = bookService.findBookByTitle(title);
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookFound);
    }

    @Operation(summary = "Find book by author")
    @GetMapping("/findByAuthor")
    public ResponseData<List<BookDetailResponse>> getBookByAuthor(@RequestParam String author) {

        List<BookDetailResponse> bookFound = bookService.findBookByAuthor(author);
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookFound);

    }

    @Operation(summary = "Find book by title and author")
    @GetMapping("/findByTitleAndAuthor")
    public ResponseData<List<BookDetailResponse>> getBookByAuthor(@RequestParam String title, @RequestParam String author) {

        List<BookDetailResponse> bookFound = bookService.findBookByTitleAndAuthor(title, author);
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookFound);

    }

    @Operation(summary = "Show all books")
    @GetMapping("/all")
    public ResponseData<PageResponse> getAllBook(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {

        PageResponse bookFound = bookService.findALlBook(pageNo, pageSize);
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookFound);

    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.book.deleteBookById'))")
    @Operation(summary = "Delete book by Id")
    @SecurityRequirement(name = "BearerAuth")
    @DeleteMapping("/delete")
    public ResponseData<Book> deleteBookById(@RequestParam Long id) {

        Book book = bookService.deleteBookById(id);
        log.info("Book with id {} deleted successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("book.delete.success"), book);

    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.book.updateBookById'))")
    @Operation(summary = "Update book by Id")
    @SecurityRequirement(name = "BearerAuth")
    @PutMapping("/update")
    public ResponseData<Book> updateBookById(@RequestParam Long id, @Valid @RequestBody BookRequestDTO request) {
        Book book = bookService.updateBook(request, id);
        log.info("Book with id {} updated successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("book.update.success"), book);
    }
}
