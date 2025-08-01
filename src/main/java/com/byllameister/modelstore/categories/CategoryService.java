package com.byllameister.modelstore.categories;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.products.ProductRepository;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final PageableValidator pageableValidator;

    private final Set<String> VALID_SORT_FIELDS = Set.of("id", "name");
    private final CategoryMapper categoryMapper;
    private final ProductRepository productRepository;

    public List<CategoryDto> getAllCategories(Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);
        var categories = categoryRepository.findAll(pageable).getContent();
        return categoryMapper.toDtos(categories);
    }

    public CategoryDto getCategoryById(Long id) {
        var category = categoryRepository.findById(id).
                orElseThrow(CategoryNotFoundInQueryException::new);
        return categoryMapper.toDto(category);
    }

    public CategoryDto createCategory(@Valid CategoryDto categoryDto) {
        var category = categoryMapper.toEntity(categoryDto);
        category = categoryRepository.save(category);
        return categoryMapper.toDto(category);
    }

    public CategoryDto updateCategoryById(@Valid CategoryDto categoryDto, Long id) {
        var category = categoryRepository.findById(id).
                orElseThrow(CategoryNotFoundInQueryException::new);

        categoryMapper.update(categoryDto, category);
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
