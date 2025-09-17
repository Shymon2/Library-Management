package Library.Project.service.implement;

import Library.Project.entity.Book;
import Library.Project.entity.Category;
import Library.Project.constant.enums.ErrorCodeFail;
import Library.Project.exception.AppException;
import Library.Project.repository.CategoryRepository;
import Library.Project.dto.Request.Library.CategoryDTO;
import Library.Project.dto.Response.LibraryResponse.CategoryDetailResponse;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.service.interfaces.ICategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Category addNewCategory(CategoryDTO request) {
        if (existsByCategoryName(request.getCategoryName())) {
            log.info("Category already existed");
            throw new AppException(ErrorCodeFail.ALREADY_EXISTED);
        }
        else {
            Category category = Category.builder()
                    .categoryName(request.getCategoryName())
                    .books(new HashSet<>())
                    .build();
            categoryRepository.save(category);
            log.info("Add new category successfully");
            return category;
        }
    }

    @Override
    public CategoryDetailResponse getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeFail.NOT_FOUND));
        return CategoryDetailResponse.builder()
                .categoryName(category.getCategoryName())
                .build();
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        if(category == null)
            throw new AppException(ErrorCodeFail.NOT_FOUND);
        return category;
    }

    @Override
    public PageResponse getAllCategories(int pageNo, int pageSize) {
        Page<Category> categoriesFound = categoryRepository.findAll(PageRequest.of(pageNo - 1, pageSize));
        if (categoriesFound.isEmpty()) {
            throw new AppException(ErrorCodeFail.NOT_FOUND);
        }
        List<CategoryDetailResponse> categoryList = categoriesFound.stream().map(c -> CategoryDetailResponse.builder()
                        .categoryName(c.getCategoryName())
                        .build())
                .toList();

        return PageResponse.builder()
                .pageNo(pageNo)
                .pageSize(pageSize)
                .totalPage(categoriesFound.getTotalPages())
                .items(categoryList)
                .build();
    }

    @Override
    public Category updateCategory(CategoryDTO request, Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeFail.NOT_FOUND));
        category.setCategoryName(request.getCategoryName());
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category deleteCategory(Long id) {
        Category categoryFound = categoryRepository.findById(id).orElseThrow(() ->
                new AppException(ErrorCodeFail.NOT_FOUND));
        for(Book book : categoryFound.getBooks()){
            book.getCategories().remove(categoryFound);
        }
        categoryRepository.save(categoryFound);
        categoryRepository.delete(categoryFound);
        return categoryFound;
    }

    @Override
    public Boolean existsByCategoryName(String categoryName) {
        return categoryRepository.existsByCategoryName(categoryName);
    }
}
