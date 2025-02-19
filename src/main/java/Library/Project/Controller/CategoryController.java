package Library.Project.Controller;

import Library.Project.Configuration.Translator;
import Library.Project.Exception.AlreadyExistsException;
import Library.Project.Model.Category;
import Library.Project.Service.CategoryService.CategoryService;
import Library.Project.dto.Request.CategoryDTO;
import Library.Project.dto.Response.CategoryDetailResponse;
import Library.Project.dto.Response.PageResponse;
import Library.Project.dto.Response.ResponseData;
import Library.Project.dto.Response.ResponseError;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/category")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Controller")
public class CategoryController {
    private final CategoryService categoryService;

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.category.createNewCategory'))")
    @Operation(summary = "Add new category", description = "New category")
    @PostMapping("/add")
    public ResponseData<Category> addNewCategory(@Valid @RequestBody CategoryDTO request) {
        Category newCategory = categoryService.addNewCategory(request);
        return new ResponseData<>(1000, Translator.toLocale("category.add.success"), newCategory);
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.category.getCateById'))")
    @Operation(summary = "Get category by Id", description = "Id must be positive")
    @GetMapping("/findById")
    public ResponseData<CategoryDetailResponse> getCategoryById(@RequestParam @Min(0) Long id) {
        CategoryDetailResponse categoryFound = categoryService.getCategoryById(id);
        log.info("Category with id {} found successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.found.success"), categoryFound);
    }

    @Operation(summary = "Get all category", description = "Show all category of library")
    @GetMapping("/all")
    public ResponseData<PageResponse> getAllCategories(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {
        PageResponse categoryFound = categoryService.getAllCategories(pageNo, pageSize);
        log.info("All category have shown");
        return new ResponseData<>(1000, Translator.toLocale("category.show.all"), categoryFound);

    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.category.updateCategory'))")
    @Operation(summary = "Update category by Id", description = "Id must be positive")
    @PutMapping("/update")
    public ResponseData<Category> updateCategoryById(@RequestParam @Min(0) Long id, @Valid @RequestBody CategoryDTO request) {
        Category category = categoryService.updateCategory(request, id);
        log.info("Category with id {} updated successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.update.success"), category);
    }

    @PreAuthorize(value = "hasRole('ADMIN') || hasAuthority(@roleService.getRoleForApi('library.category.deleteCategory'))")
    @Operation(summary = "Delete category by Id", description = "Id must be positive")
    @DeleteMapping("/delete")
    public ResponseData<Category> deleteCategoryById(@RequestParam @Min(0) Long id) {
        Category category = categoryService.deleteCategory(id);
        log.info("Category with id {} deleted successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.delete.success"), category);
    }
}
