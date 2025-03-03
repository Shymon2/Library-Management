package Library.Project.service.interfaces;

import Library.Project.entity.Category;
import Library.Project.dto.Request.CategoryDTO;
import Library.Project.dto.Response.CategoryDetailResponse;
import Library.Project.dto.Response.PageResponse;

public interface ICategoryService {

    Category addNewCategory(CategoryDTO category);

    CategoryDetailResponse getCategoryById(Long id);

    Category getCategoryByName(String categoryName);

    PageResponse getAllCategories(int pageNo, int pageSize);

    Category updateCategory(CategoryDTO request, Long id);

    Category deleteCategory(Long id);

    Boolean existsByCategoryName(String categoryName);
}
