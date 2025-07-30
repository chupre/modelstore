package com.byllameister.modelstore.mappers;

import com.byllameister.modelstore.dtos.CategoryDto;
import com.byllameister.modelstore.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    List<CategoryDto> toDtos(List<Category> categories);

    Category toEntity(CategoryDto categoryDto);

    @Mapping(target = "id", ignore = true)
    void update(CategoryDto categoryDto, @MappingTarget Category category);
}
