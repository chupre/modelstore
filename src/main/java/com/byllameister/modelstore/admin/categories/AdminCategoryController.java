package com.byllameister.modelstore.admin.categories;

import com.byllameister.modelstore.categories.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/admin/categories")
@AllArgsConstructor
@Tag(name = "Admin Categories", description = "Admin methods related to categories")
public class AdminCategoryController {
    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CreateCategoryRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var category = categoryService.createCategory(request);
        var uri =  uriComponentsBuilder.path("/categories/{id}").
                buildAndExpand(category.getId()).
                toUri();

        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody UpdateCategoryRequest request,
            @PathVariable Long id
    ) {
        var category = categoryService.updateCategoryById(request, id);
        return ResponseEntity.ok(category);
    }

    @ExceptionHandler(CategoryNotFoundInQueryException.class)
    public ResponseEntity<Void> handleCategoryNotFoundInQuery() {
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
