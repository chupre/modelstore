package com.byllameister.modelstore.users;

import com.byllameister.modelstore.admin.users.UpdateUserRequest;
import com.byllameister.modelstore.admin.users.UserExposedResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    UserExposedResponse toExposedResponse(User user);

    User toEntity(RegisterUserRequest request);

    void update(UpdateUserRequest request, @MappingTarget User user);
}
