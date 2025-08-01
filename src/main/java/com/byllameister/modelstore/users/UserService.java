package com.byllameister.modelstore.users;

import com.byllameister.modelstore.common.PageableValidator;
import com.byllameister.modelstore.products.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PageableValidator pageableValidator;
    private final ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

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

    public UserDto createUser(@Valid RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.BUYER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, @Valid UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        if (!user.getUsername().equals(request.getUsername()) &&
                userRepository.existsByUsername(request.getUsername())) {
            throw new DuplicateUsernameException();
        }

        if (!user.getEmail().equals(request.getEmail()) &&
            userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateEmailException();
        }

        userMapper.update(request, user);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    @Transactional
    public void deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);

        productRepository.deleteByOwner(user);
        userRepository.delete(user);
    }

    public UserDto getCurrentUser() {
        var user = userRepository.findById(User.getCurrentUserId()).orElseThrow(UserNotFoundException::new);
        return userMapper.toDto(user);
    }
}
