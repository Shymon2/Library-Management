package Library.Project.Service.CategoryService;

import Library.Project.Configuration.Translator;
import Library.Project.Exception.AlreadyExistsException;
import Library.Project.Exception.ResourcesNotFoundException;
import Library.Project.Model.Book;
import Library.Project.Model.Category;
import Library.Project.Repository.CategoryRepository;
import Library.Project.dto.Request.CategoryDTO;
import Library.Project.dto.Response.CategoryDetailResponse;
import Library.Project.dto.Response.PageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
            throw new AlreadyExistsException(Translator.toLocale("category.existed"));
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
                new ResourcesNotFoundException(Translator.toLocale("category.not.found")));
        return CategoryDetailResponse.builder()
                .categoryName(category.getCategoryName())
                .build();
    }

    @Override
    public Category getCategoryByName(String categoryName) {
        Category category = categoryRepository.findByCategoryName(categoryName);
        if(category == null)
            throw new ResourcesNotFoundException(Translator.toLocale("category.not.found"));
        return category;
    }

    @Override
    public PageResponse getAllCategories(int pageNo, int pageSize) {
        Page<Category> categoriesFound = categoryRepository.findAll(PageRequest.of(pageNo - 1, pageSize));
        if (categoriesFound.isEmpty()) {
            throw new ResourcesNotFoundException(Translator.toLocale("category.list.empty"));
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
                new ResourcesNotFoundException(Translator.toLocale("category.not.found")));
        category.setCategoryName(request.getCategoryName());
        categoryRepository.save(category);
        return category;
    }

    @Override
    public Category deleteCategory(Long id) {
        Category categoryFound = categoryRepository.findById(id).orElseThrow(() ->
                new ResourcesNotFoundException(Translator.toLocale("category.not.found")));
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
