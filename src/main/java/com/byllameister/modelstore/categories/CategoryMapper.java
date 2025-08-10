package com.byllameister.modelstore.categories;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryDto toDto(Category category);

    Category toEntity(CreateCategoryRequest categoryDto);

    @Mapping(target = "id", ignore = true)
    void update(UpdateCategoryRequest request, @MappingTarget Category category);
}
