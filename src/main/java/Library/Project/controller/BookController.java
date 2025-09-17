package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.GeneralPayload;
import Library.Project.dto.Request.Library.BookSearchRequest;
import Library.Project.entity.Book;
import Library.Project.dto.Request.Library.BookRequestDTO;
import Library.Project.dto.Response.LibraryResponse.BookDetailResponse;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.service.RestfulResponseFactory;
import Library.Project.service.interfaces.IBookService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/${base.app.context}/api/v1/book")
@Validated
@RequiredArgsConstructor
@Tag(name = "Book Controller")
public class BookController {
    private final IBookService bookService;

    @Operation(summary = "Find book by ID", description = "ID must be positive")
    @SecurityRequirements
    @GetMapping("/find-by-name")
    public ResponseEntity<GeneralPayload<BookDetailResponse>> getBookByName(@RequestParam String name) {
        return RestfulResponseFactory.of(bookService.findBookByName(name));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add new Book")
    @PostMapping("/add")
    public ResponseData<BookDetailResponse> addNewBook(HttpServletRequest httpServletRequest, @Valid @RequestBody BookRequestDTO request) {
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
    public ResponseData<PageResponse<List<BookDetailResponse>>> findByCriteria(@RequestBody BookSearchRequest request,
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

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete book by Id")
    @DeleteMapping("/delete")
    public ResponseData<String> deleteBookById(HttpServletRequest httpServletRequest, @RequestParam Long id) {
        bookService.deleteBookById(id);
        return new ResponseData<>(1000, Translator.toLocale("book.delete.success"));

    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update book by Id")
    @PutMapping("/update")
    public ResponseData<BookDetailResponse> updateBookById(HttpServletRequest httpServletRequest, @RequestParam Long id, @Valid @RequestBody BookRequestDTO request) {
        Book book = bookService.updateBook(request, id);
        return new ResponseData<>(1000, Translator.toLocale("book.update.success"), bookService.convertToResponse(book));
    }

}
