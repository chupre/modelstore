package com.byllameister.modelstore.users;

import com.byllameister.modelstore.admin.users.UpdateUserRequest;
import com.byllameister.modelstore.admin.users.UserExposedResponse;
import com.byllameister.modelstore.products.ProductRepository;
import com.byllameister.modelstore.users.profiles.UserProfile;
import com.byllameister.modelstore.users.profiles.UserProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private UserMapper userMapper;
    @Mock private UserProfileRepository userProfileRepository;
    @Mock private ProductRepository productRepository;
    @Mock private PasswordEncoder passwordEncoder;

    @InjectMocks private UserService userService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllUsers_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        var result = userService.getAllUsers(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getAllUsersExposed_shouldReturnMappedPage() {
        Pageable pageable = PageRequest.of(0, 10);
        User user = new User();
        Page<User> page = new PageImpl<>(List.of(user));

        when(userRepository.findAll(pageable)).thenReturn(page);
        when(userMapper.toExposedResponse(user)).thenReturn(new UserExposedResponse(1L,"user","email", Role.BUYER,false, LocalDate.now()));

        var result = userService.getAllUsersExposed(pageable);

        assertThat(result.getTotalElements()).isEqualTo(1);
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getUserById_shouldReturnMappedUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        var result = userService.getUserById(1L);

        assertThat(result).isNotNull();
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserById(1L));
    }

    @Test
    void getUserExposedById_shouldReturnMappedUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userMapper.toExposedResponse(user)).thenReturn(new UserExposedResponse(1L,"user","email",Role.BUYER,true, LocalDate.now()));

        var result = userService.getUserExposedById(1L);

        assertThat(result).isNotNull();
    }

    @Test
    void getUserExposedById_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.getUserExposedById(1L));
    }

    @Test
    void createUser_shouldThrowIfEmailExists() {
        RegisterUserRequest request = new RegisterUserRequest("user","test@example.com","Password1");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_shouldThrowIfUsernameExists() {
        RegisterUserRequest request = new RegisterUserRequest("user","test@example.com","Password1");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(userRepository.existsByUsername("user")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class, () -> userService.createUser(request));
    }

    @Test
    void createUser_shouldSaveUserAndProfile() {
        RegisterUserRequest request = new RegisterUserRequest("user","test@example.com","Password1");
        User user = new User();
        user.setPassword("Password1");
        when(userRepository.existsByEmail(any())).thenReturn(false);
        when(userRepository.existsByUsername(any())).thenReturn(false);
        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("Password1")).thenReturn("encodedPassword");
        when(userMapper.toResponse(user)).thenReturn(new UserResponse());

        var result = userService.createUser(request);

        assertThat(result).isNotNull();
        assertThat(user.getPassword()).isEqualTo("encodedPassword");
        verify(userRepository).save(user);
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void updateUser_shouldThrowIfNotFound() {
        UpdateUserRequest request = new UpdateUserRequest("user","email@example.com","BUYER");
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void updateUser_shouldThrowIfUsernameExists() {
        User user = new User();
        user.setUsername("oldUser");
        user.setEmail("oldEmail@example.com");

        UpdateUserRequest request = new UpdateUserRequest("newUser","email@example.com","BUYER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("newUser")).thenReturn(true);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);

        assertThrows(DuplicateUsernameException.class, () -> userService.updateUser(1L, request));
    }

    @Test
    void updateUser_shouldThrowIfEmailExists() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("old@example.com");

        UpdateUserRequest request = new UpdateUserRequest("user","newEmail@example.com","BUYER");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("newEmail@example.com")).thenReturn(true);

        assertThrows(DuplicateEmailException.class, () -> userService.updateUser(1L, request));
    }


    @Test
    void updateUser_shouldSaveUserAndProfile() {
        User user = new User();
        user.setUsername("user");
        user.setEmail("email@exmaple.com");
        UpdateUserRequest request = new UpdateUserRequest("user","email@example.com","BUYER");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userRepository.existsByUsername("user")).thenReturn(false);
        when(userRepository.existsByEmail("email@example.com")).thenReturn(false);
        when(userProfileRepository.findById(any())).thenReturn(Optional.empty());
        when(userMapper.toExposedResponse(user)).thenReturn(new UserExposedResponse(1L,"user","email@example.com",Role.BUYER,true, LocalDate.now()));

        var result = userService.updateUser(1L, request);

        assertThat(result).isNotNull();
        verify(userRepository).save(user);
        verify(userProfileRepository).save(any(UserProfile.class));
    }

    @Test
    void deleteUser_shouldThrowIfNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.deleteUser(1L));
    }

    @Test
    void deleteUser_shouldDeleteProductsAndUser() {
        User user = new User();
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        userService.deleteUser(1L);

        verify(productRepository).deleteByOwner(user);
        verify(userRepository).delete(user);
    }

    @Test
    void getCurrentUser_shouldThrowIfNotFound() {
        try (MockedStatic<User> utilities = mockStatic(User.class)) {
            utilities.when(User::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(1L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.getCurrentUser());
        }
    }

    @Test
    void getCurrentUser_shouldReturnUser() {
        try (MockedStatic<User> utilities = mockStatic(User.class)) {
            User user = new User();
            utilities.when(User::getCurrentUserId).thenReturn(1L);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(userMapper.toResponse(user)).thenReturn(new UserResponse());

            var result = userService.getCurrentUser();

            assertThat(result).isNotNull();
        }
    }
}