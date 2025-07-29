package com.byllameister.modelstore.services;

import com.byllameister.modelstore.dtos.UserDto;
import com.byllameister.modelstore.exceptions.UserNotFoundException;
import com.byllameister.modelstore.mappers.UserMapper;
import com.byllameister.modelstore.repositories.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public List<UserDto> getAllUsers() {
        var users = userRepository.findAll();
        return userMapper.toDtos(users);
    }

    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }
}
