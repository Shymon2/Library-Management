package Library.Project.service.interfaces;

import Library.Project.dto.response.LibraryResponse.BookTrendResponse;
import Library.Project.dto.response.LibraryResponse.CategoryStaticResponse;

import java.util.List;

public interface IStaticService {
    List<CategoryStaticResponse> numBookByCate();

    List<BookTrendResponse> top5BookOrder();
}
