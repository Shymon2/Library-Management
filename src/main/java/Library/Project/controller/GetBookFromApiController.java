package Library.Project.controller;

import Library.Project.dto.GeneralPayload;
import Library.Project.dto.POJO.BookGetFromApi;
import Library.Project.service.RestfulResponseFactory;
import Library.Project.service.implement.BookGetFromApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/get-book-from-api")
public class GetBookFromApiController {
    private final BookGetFromApiService bookGetFromApiService;

    @GetMapping("/all")
    public ResponseEntity<GeneralPayload<List<BookGetFromApi>>> getAllBook() {
        return RestfulResponseFactory.of(bookGetFromApiService.getBook());
    }
}
