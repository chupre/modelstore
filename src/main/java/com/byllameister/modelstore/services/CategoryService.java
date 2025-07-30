package com.byllameister.modelstore.services;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.dtos.CategoryDto;
import com.byllameister.modelstore.exceptions.CategoryNotFoundInBodyException;
import com.byllameister.modelstore.exceptions.CategoryNotFoundInQueryException;
import com.byllameister.modelstore.mappers.CategoryMapper;
import com.byllameister.modelstore.repositories.CategoryRepository;
import com.byllameister.modelstore.repositories.ProductRepository;
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
                orElseThrow(CategoryNotFoundInBodyException::new);
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
                orElseThrow(CategoryNotFoundInBodyException::new);

        var products = category.getProducts();
        productRepository.deleteAll(products);

        categoryRepository.delete(category);
    }
}
