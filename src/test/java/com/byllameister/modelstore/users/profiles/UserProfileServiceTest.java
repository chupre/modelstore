package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.upload.UploadService;
import com.byllameister.modelstore.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class UserProfileServiceTest {

    @Mock
    private UserProfileRepository userProfileRepository;

    @Mock
    private UserProfileMapper userProfileMapper;

    @Mock
    private UploadService uploadService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserProfileService userProfileService;

    private User user;
    private UserProfile profile;
    private UserProfileDto profileDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testUser");

        profile = new UserProfile();
        profile.setId(1L);
        profile.setUser(user);
        profile.setName("testUser");
        profile.setAvatarUrl("oldAvatar.png");
        profile.setBio("Old bio");

        profileDto = new UserProfileDto();
        profileDto.setId(profile.getId());
        profileDto.setUserId(user.getId());
        profileDto.setName(profile.getName());
        profileDto.setAvatarUrl(profile.getAvatarUrl());
        profileDto.setBio(profile.getBio());
    }

    // --- getUserProfile ---

    @Test
    void getUserProfile_shouldReturnDto() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(userProfileMapper.toDto(profile)).thenReturn(profileDto);

        UserProfileDto result = userProfileService.getUserProfile(1L);

        assertThat(result).isEqualTo(profileDto);
        verify(userProfileRepository).findByUserId(1L);
        verify(userProfileMapper).toDto(profile);
    }

    @Test
    void getUserProfile_shouldThrowWhenNotFound() {
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(UserProfileNotFoundException.class, () -> userProfileService.getUserProfile(1L));
    }

    // --- updateProfile ---

    @Test
    void updateProfile_shouldUpdateUsernameAndBio() throws IOException {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("newUsername");
        request.setBio("New bio");

        when(userRepository.existsByUsername("newUsername")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(userProfileMapper.toDto(profile)).thenReturn(profileDto);

        UserProfileDto result = userProfileService.updateProfile(1L, request);

        // verify changes
        assertThat(profile.getName()).isEqualTo("newUsername");
        assertThat(profile.getBio()).isEqualTo("New bio");
        assertThat(user.getUsername()).isEqualTo("newUsername");

        verify(userRepository).save(user);
        verify(userProfileRepository).save(profile);
        verify(userProfileMapper).toDto(profile);
    }

    @Test
    void updateProfile_shouldUpdateAvatar() throws IOException {
        MockMultipartFile file = new MockMultipartFile("avatar", "avatar.png", "image/png", "data".getBytes());

        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setAvatarImage(file);

        when(userRepository.existsByUsername(anyString())).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.of(profile));
        when(uploadService.uploadImage(file)).thenReturn("newAvatar.png");
        when(userProfileMapper.toDto(profile)).thenReturn(profileDto);

        UserProfileDto result = userProfileService.updateProfile(1L, request);

        verify(uploadService).deleteFile("oldAvatar.png");
        verify(uploadService).uploadImage(file);
        assertThat(profile.getAvatarUrl()).isEqualTo("newAvatar.png");
        verify(userProfileRepository).save(profile);
    }

    @Test
    void updateProfile_shouldThrowDuplicateUsername() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("existingUser");

        when(userRepository.existsByUsername("existingUser")).thenReturn(true);

        assertThrows(DuplicateUsernameException.class,
                () -> userProfileService.updateProfile(1L, request));
    }

    @Test
    void updateProfile_shouldThrowUserNotFound() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("newUser");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(UserNotFoundException.class,
                () -> userProfileService.updateProfile(1L, request));
    }

    @Test
    void updateProfile_shouldThrowProfileNotFound() {
        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("newUser");

        when(userRepository.existsByUsername("newUser")).thenReturn(false);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(userProfileRepository.findByUserId(1L)).thenReturn(Optional.empty());

        assertThrows(UserProfileNotFoundException.class,
                () -> userProfileService.updateProfile(1L, request));
    }
}
