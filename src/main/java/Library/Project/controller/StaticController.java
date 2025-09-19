package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.response.LibraryResponse.BookTrendResponse;
import Library.Project.dto.response.LibraryResponse.CategoryStaticResponse;
import Library.Project.service.interfaces.IStaticService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@SecurityRequirements
@Tag(name = "Static Controller")
@RequestMapping("/static")
public class StaticController {
    private final IStaticService staticService;

    @Operation(summary = "Get number of book by category")
    @GetMapping("/book-by-cate")
    public ResponseData<List<CategoryStaticResponse>> numBookByCate(){
        return new ResponseData<>(1000, Translator.toLocale("static.book.category"), staticService.numBookByCate());
    }

    @Operation(summary = "Get top 5 book by order quantity", description = "In decreasing way")
    @GetMapping("/book-by-order-quantity")
    public ResponseData<List<BookTrendResponse>> top5BookOrder(){
        return new ResponseData<>(1000, Translator.toLocale("static.top5.bookOrder"), staticService.top5BookOrder());
    }
}
