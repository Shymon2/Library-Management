package Library.Project.controller;

import Library.Project.configuration.Translator;
import Library.Project.dto.GeneralPayload;
import Library.Project.entity.Category;
import Library.Project.dto.request.library.CategoryDTO;
import Library.Project.dto.response.LibraryResponse.CategoryDetailResponse;
import Library.Project.dto.response.ApiResponse.PageResponse;
import Library.Project.service.RestfulResponseFactory;
import Library.Project.service.interfaces.ICategoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/${base.app.context}/api/v1/category")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Category Controller")
public class CategoryController {
    private final ICategoryService categoryService;

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Add new category", description = "New category")
    @PostMapping("/add")
    public ResponseEntity<GeneralPayload<Category>> addNewCategory(HttpServletRequest httpServletRequest, @Valid @RequestBody CategoryDTO request) {
        return RestfulResponseFactory.of(categoryService.addNewCategory(request));
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Get category by Id", description = "Id must be positive")
    @GetMapping("/find-by-id")
    public ResponseEntity<GeneralPayload<CategoryDetailResponse>> getCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id) {
        CategoryDetailResponse categoryFound = categoryService.getCategoryById(id);
        log.info("Category with id {} found successfully", id);
        String a;
        a.equals(b);
        return new ResponseData<>(1000, Translator.toLocale("category.found.success"), categoryFound);
    }

    @SecurityRequirements
    @Operation(summary = "Get all category", description = "Show all category of library")
    @GetMapping("/all")
    public ResponseEntity<GeneralPayload<PageResponse<List<Category>>>> getAllCategories(@RequestParam @Min(1) int pageNo, @RequestParam @Min(1) int pageSize) {
        PageResponse categoryFound = categoryService.getAllCategories(pageNo, pageSize);
        log.info("All category have shown");
        return new ResponseData<>(1000, Translator.toLocale("category.show.all"), categoryFound);

    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Update category by Id", description = "Id must be positive")
    @PutMapping("/update")
    public ResponseEntity<GeneralPayload<Category>> updateCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id, @Valid @RequestBody CategoryDTO request) {
        Category category = categoryService.updateCategory(request, id);
        log.info("Category with id {} updated successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.update.success"), category);
    }

    @PreAuthorize("fileRole(#httpServletRequest)")
    @Operation(summary = "Delete category by Id", description = "Id must be positive")
    @DeleteMapping("/delete")
    public ResponseEntity<GeneralPayload<Category>> deleteCategoryById(HttpServletRequest httpServletRequest, @RequestParam @Min(0) Long id) {
        Category category = categoryService.deleteCategory(id);
        log.info("Category with id {} deleted successfully", id);
        return new ResponseData<>(1000, Translator.toLocale("category.delete.success"), category);
    }
}
