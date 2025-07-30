package com.byllameister.modelstore.users;

import com.byllameister.modelstore.common.PageableValidator;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageableValidator pageableValidator;

    private final Set<String> VALID_SORT_FIELDS = Set.of("id", "username", "email");

    public List<UserDto> getAllUsers(Pageable pageable) {
        pageableValidator.validate(pageable, VALID_SORT_FIELDS);
        var users = userRepository.findAll(pageable).getContent();
        return userMapper.toDtos(users);
    }

    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }
}
