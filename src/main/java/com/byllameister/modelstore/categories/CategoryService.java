package com.byllameister.modelstore.categories;

import com.byllameister.modelstore.admin.categories.CreateCategoryRequest;
import com.byllameister.modelstore.admin.categories.UpdateCategoryRequest;
import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.products.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;

    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    public Page<CategoryDto> getAllCategories(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.CATEGORY_SORT_FIELDS);
        var categories = categoryRepository.findAll(pageable);
        return categories.map(categoryMapper::toDto);
    }

    public CategoryDto getCategoryById(Long id) {
        var category = categoryRepository.findById(id).
                orElseThrow(CategoryNotFoundInQueryException::new);
        return categoryMapper.toDto(category);
    }

    public CategoryDto createCategory(@Valid CreateCategoryRequest request) {
        var category = categoryMapper.toEntity(request);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public CategoryDto updateCategoryById(@Valid UpdateCategoryRequest request, Long id) {
        var category = categoryRepository.findById(id).
                orElseThrow(CategoryNotFoundInQueryException::new);

        categoryMapper.update(request, category);
        category = categoryRepository.save(category);

        return categoryMapper.toDto(category);
    }

    public void deleteCategory(Long id) {
        var category = categoryRepository.findById(id).
                orElseThrow(CategoryNotFoundInQueryException::new);

        var products = category.getProducts();
        productRepository.deleteAll(products);

        categoryRepository.delete(category);
    }
}
