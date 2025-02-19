package Library.Project.Service.StaticService;

import Library.Project.Configuration.Translator;
import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Repository.BookRepository;
import Library.Project.Repository.CategoryRepository;
import Library.Project.dto.Request.BookTrendDTO;
import Library.Project.dto.Request.BooksByCateDTO;
import Library.Project.dto.Response.BookTrendResponse;
import Library.Project.dto.Response.CategoryStaticResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaticService implements IStaticService{
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Override
    public List<CategoryStaticResponse> numBookByCate() {
        List<BooksByCateDTO> list = categoryRepository.findNumBookByCategoryName();
        if(list.isEmpty())
            throw new ResourcesNotFoundException(Translator.toLocale("static.list.empty"));
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
            throw new ResourcesNotFoundException(Translator.toLocale("static.list.empty"));
        return list.stream().map(a ->
                BookTrendResponse.builder()
                        .title(a.getTitle())
                        .author(a.getAuthor())
                        .orderQuantity(a.getOrderQuantity())
                        .build()).toList();
    }
}
