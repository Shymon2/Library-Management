package Library.Project.service.implement;

import Library.Project.enums.ErrorCode;
import Library.Project.exception.AppException;
import Library.Project.repository.BookRepository;
import Library.Project.repository.CategoryRepository;
import Library.Project.dto.Request.BookTrendDTO;
import Library.Project.dto.Request.BooksByCateDTO;
import Library.Project.dto.Response.BookTrendResponse;
import Library.Project.dto.Response.CategoryStaticResponse;
import Library.Project.service.interfaces.IStaticService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaticService implements IStaticService {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public List<CategoryStaticResponse> numBookByCate() {
        List<BooksByCateDTO> list = categoryRepository.findNumBookByCategoryName();
        if(list.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND);
        return list.stream().map(a ->
             CategoryStaticResponse.builder()
                    .category(a.getCategory())
                     .quantity(a.getQuantity())
                    .build()).toList();
    }

    @Override
    public List<BookTrendResponse> top5BookOrder() {
        List<BookTrendDTO> list = bookRepository.findTop5BookOrder();
        if(list.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND);
        return list.stream().map(a ->
                BookTrendResponse.builder()
                        .title(a.getTitle())
                        .author(a.getAuthor())
                        .orderQuantity(a.getOrderQuantity())
                        .build()).toList();
    }
}
