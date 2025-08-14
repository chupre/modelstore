package com.byllameister.modelstore.users;

import com.byllameister.modelstore.admin.users.UpdateUserRequest;
import com.byllameister.modelstore.admin.users.UserExposedResponse;
import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.products.ProductRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final ProductRepository productRepository;
    private PasswordEncoder passwordEncoder;

    public Page<UserDto> getAllUsers(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.USER_SORT_FIELDS);
        var users = userRepository.findAll(pageable);
        return users.map(userMapper::toDto);
    }

    public Page<UserExposedResponse> getAllUsersExposed(Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.USER_SORT_FIELDS);
        var users = userRepository.findAll(pageable);
        return users.map(userMapper::toExposedResponse);
    }

    public UserExposedResponse getUserExposedById(Long id) {
        var user = userRepository.findById(id).orElseThrow(UserNotFoundException::new);
        return userMapper.toExposedResponse(user);
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

    public UserExposedResponse updateUser(Long id, @Valid UpdateUserRequest request) {
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

        return userMapper.toExposedResponse(user);
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
