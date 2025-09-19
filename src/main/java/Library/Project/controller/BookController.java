package Library.Project.controller;

import Library.Project.dto.GeneralPayload;
import Library.Project.dto.request.library.BookSearchRequest;
import Library.Project.dto.request.library.BookRequestDTO;
import Library.Project.dto.response.LibraryResponse.BookDetailResponse;
import Library.Project.dto.response.ApiResponse.PageResponse;
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
    public ResponseEntity<GeneralPayload<Object>> getBookByName(@RequestParam String name) {
        return RestfulResponseFactory.of(bookService.findBookByName(name));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add new Book")
    @PostMapping("/add")
    public ResponseEntity<GeneralPayload<Object>> addNewBook(HttpServletRequest httpServletRequest, @Valid @RequestBody BookRequestDTO request) {
        return RestfulResponseFactory.of(bookService.addNewBook(request));
    }

    @Operation(summary = "Find book by criteria")
    @SecurityRequirements
    @PostMapping("/find-by-criteria")
    public ResponseEntity<GeneralPayload<PageResponse<List<BookDetailResponse>>>> findByCriteria(@RequestBody BookSearchRequest request,
                                                                               @RequestParam int pageNo,
                                                                               @RequestParam int pageSize){
        return RestfulResponseFactory.of(bookService.findBooksByCriteria(request, pageNo, pageSize));
    }

    @Operation(summary = "Show all books")
    @SecurityRequirements
    @GetMapping("/all")
    public ResponseEntity<GeneralPayload<PageResponse<List<BookDetailResponse>>>> getAllBook(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {
        return RestfulResponseFactory.of(bookService.findALlBook(pageNo, pageSize));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete book by Id")
    @DeleteMapping("/delete")
    public ResponseEntity<GeneralPayload<Object>> deleteBookById(HttpServletRequest httpServletRequest, @RequestParam Long id) {
        return RestfulResponseFactory.of(bookService.deleteBookById(id));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update book by Id")
    @PutMapping("/update")
    public ResponseEntity<GeneralPayload<Object>> updateBookById(HttpServletRequest httpServletRequest,
                                                                             @RequestParam Long id,
                                                                             @Valid @RequestBody BookRequestDTO request) {
        return RestfulResponseFactory.of(bookService.updateBook(request, id));
    }

}
