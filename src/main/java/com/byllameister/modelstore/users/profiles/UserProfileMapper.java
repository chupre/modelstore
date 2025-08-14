package com.byllameister.modelstore.users.profiles;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    @Mapping(target = "userId", source = "user.id")
    UserProfileDto toDto(UserProfile user);
}
