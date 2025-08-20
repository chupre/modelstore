package com.byllameister.modelstore.sellers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface SellerMapper {
    @Mapping(source = "userId", target = "user.id")
    Seller toEntity(CreateSellerRequest request);

    @Mapping(source = "user.id", target = "userId")
    SellerResponse toDto(Seller seller);

    void update(UpdateSellerRequest request, @MappingTarget Seller seller);
}
