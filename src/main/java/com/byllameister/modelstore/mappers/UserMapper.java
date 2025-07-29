package com.byllameister.modelstore.mappers;

import com.byllameister.modelstore.dtos.UserDto;
import com.byllameister.modelstore.entities.User;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
    List<UserDto> toDtos(Iterable<User> users);
}
