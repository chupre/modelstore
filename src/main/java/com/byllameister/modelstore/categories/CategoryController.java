package com.byllameister.modelstore.categories;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        return categoryService.getAllCategories(pageable);
    }

    @GetMapping("/{id}")
    public CategoryDto getCategoryById(@PathVariable Long id) {
        return categoryService.getCategoryById(id);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> createCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var category = categoryService.createCategory(categoryDto);
        var uri =  uriComponentsBuilder.path("/categories/{id}").
                buildAndExpand(category.getId()).
                toUri();

        return ResponseEntity.created(uri).body(category);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> updateCategory(
            @Valid @RequestBody CategoryDto categoryDto,
            @PathVariable Long id
    ) {
        var category = categoryService.updateCategoryById(categoryDto, id);
        return ResponseEntity.ok(category);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(CategoryNotFoundInQueryException.class)
    public ResponseEntity<Void> handleCategoryNotFoundInQuery() {
        return ResponseEntity.notFound().build();
    }
}
