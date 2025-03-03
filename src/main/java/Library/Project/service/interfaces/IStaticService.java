package Library.Project.service.interfaces;

import Library.Project.dto.Response.BookTrendResponse;
import Library.Project.dto.Response.CategoryStaticResponse;

import java.util.List;

public interface IStaticService {
    List<CategoryStaticResponse> numBookByCate();

    List<BookTrendResponse> top5BookOrder();
}
