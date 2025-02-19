package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Service.StaticService.StaticService;
import Library.Project.dto.Response.BookTrendResponse;
import Library.Project.dto.Response.CategoryStaticResponse;
import Library.Project.dto.Response.ResponseData;
import io.swagger.v3.oas.annotations.Operation;
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
@Tag(name = "Static Controller")
@RequestMapping("/static")
public class StaticController {
    private final StaticService staticService;

    @Operation(summary = "Get number of book by category")
    @GetMapping("/bookByCate")
    public ResponseData<List<CategoryStaticResponse>> numBookByCate(){
        return new ResponseData<>(1000, Translator.toLocale("static.book.category"), staticService.numBookByCate());
    }

    @Operation(summary = "Get top 5 book by order quantity", description = "In decreasing way")
    @GetMapping("/bookByOrderQuantity")
    public ResponseData<List<BookTrendResponse>> top5BookOrder(){
        return new ResponseData<>(1000, Translator.toLocale("static.top5.bookOrder"), staticService.top5BookOrder());
    }
}
