package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.entity.Category;
import Library.Project.service.implement.CategoryService;
import Library.Project.dto.Request.Library.CategoryDTO;
import Library.Project.dto.Response.LibraryResponse.CategoryDetailResponse;
import Library.Project.dto.Response.ApiResponse.PageResponse;
import Library.Project.dto.Response.ApiResponse.ResponseData;
import Library.Project.service.interfaces.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
    private final ICategoryService categoryService;

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add new category", description = "New category")
    @PostMapping("/add")
    public ResponseData<Category> addNewCategory(HttpServletRequest httpServletRequest, @Valid @RequestBody CategoryDTO request) {
        Category newCategory = categoryService.addNewCategory(request);
        return new ResponseData<>(1000, Translator.toLocale("category.add.success"), newCategory);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get category by Id", description = "Id must be positive")
    @GetMapping("/find-by-id")
    public ResponseData<CategoryDetailResponse> getCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id) {
        CategoryDetailResponse categoryFound = categoryService.getCategoryById(id);
        log.info("Category with id {} found successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.found.success"), categoryFound);
    }

    @SecurityRequirements
    @Operation(summary = "Get all category", description = "Show all category of library")
    @GetMapping("/all")
    public ResponseData<PageResponse> getAllCategories(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {
        PageResponse categoryFound = categoryService.getAllCategories(pageNo, pageSize);
        log.info("All category have shown");
        return new ResponseData<>(1000, Translator.toLocale("category.show.all"), categoryFound);

    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update category by Id", description = "Id must be positive")
    @PutMapping("/update")
    public ResponseData<Category> updateCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id, @Valid @RequestBody CategoryDTO request) {
        Category category = categoryService.updateCategory(request, id);
        log.info("Category with id {} updated successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.update.success"), category);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete category by Id", description = "Id must be positive")
    @DeleteMapping("/delete")
    public ResponseData<Category> deleteCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id) {
        Category category = categoryService.deleteCategory(id);
        log.info("Category with id {} deleted successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.delete.success"), category);
    }
}
