package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.Request.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.service.implement.BookService;
import Library.Project.dto.Request.BookRequestDTO;
import Library.Project.dto.Response.BookDetailResponse;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/book")
@Validated
@RequiredArgsConstructor
@Tag(name = "Book Controller")
public class BookController {

    private final BookService bookService;

    @Operation(summary = "Find book by ID", description = "ID must be positive")
    @SecurityRequirements
    @GetMapping("/find-by-id")
    public ResponseData<BookDetailResponse> getBookById(@RequestParam @Min(0) Long id) {
        Book bookFound = bookService.findBookById(id);
        return new ResponseData<>(1000, Translator.toLocale("book.get.success"), bookService.convertToResponse(bookFound));
    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.book.addNewBook'))")
    @Operation(summary = "Add new Book")
    @PostMapping("/add")
    public ResponseData<BookDetailResponse> addNewBook(@Valid @RequestBody BookRequestDTO request) {
        
        if (bookService.existsByTitleAndAuthor(request.getTitle(), request.getAuthor())) {
            Book updateBook = bookService.addNewBook(request);
            return new ResponseData<>(1000, Translator.toLocale("book.update.instead"), bookService.convertToResponse(updateBook));
        } else {
            Book newBook = bookService.addNewBook(request);
            return new ResponseData<>(1000, Translator.toLocale("book.add.success"), bookService.convertToResponse(newBook));
        }

    }

    @Operation(summary = "Find book by criteria")
    @SecurityRequirements
    @PostMapping("/find-by-criteria")
    public ResponseData<PageResponse> findByCriteria(@RequestBody BookSearchRequest request,
                                                     @RequestParam int pageNo,
                                                     @RequestParam int pageSize){
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookService.findBooksByCriteria(request, pageNo, pageSize));
    }

    @Operation(summary = "Show all books")
    @SecurityRequirements
    @GetMapping("/all")
    public ResponseData<PageResponse> getAllBook(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {

        PageResponse bookFound = bookService.findALlBook(pageNo, pageSize);
        return new ResponseData<>(1000, Translator.toLocale("book.found.success"), bookFound);

    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.book.deleteBookById'))")
    @Operation(summary = "Delete book by Id")
    @DeleteMapping("/delete")
    public ResponseData<String> deleteBookById(@RequestParam Long id) {
        bookService.deleteBookById(id);
        return new ResponseData<>(1000, Translator.toLocale("book.delete.success"));

    }

    @PreAuthorize(value = "hasAuthority(@roleMapping.getRoleForApi('library.book.updateBookById'))")
    @Operation(summary = "Update book by Id")
    @PutMapping("/update")
    public ResponseData<BookDetailResponse> updateBookById(@RequestParam Long id, @Valid @RequestBody BookRequestDTO request) {
        Book book = bookService.updateBook(request, id);
        return new ResponseData<>(1000, Translator.toLocale("book.update.success"), bookService.convertToResponse(book));
    }
}
