package com.byllameister.modelstore.users;

import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    List<UserDto> toDtos(Iterable<User> users);
}
