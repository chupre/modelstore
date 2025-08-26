package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.auth.JwtService;
import com.byllameister.modelstore.users.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.IOException;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserProfileService userProfileService;

    @MockitoBean
    private JwtService jwtService;

    private UserProfileDto profileDto;

    @BeforeEach
    void setUp() {
        profileDto = new UserProfileDto();
        profileDto.setId(1L);
        profileDto.setUserId(42L);
        profileDto.setName("Test User");
        profileDto.setAvatarUrl("avatar.png");
        profileDto.setBio("Bio");
    }

    @Test
    void getProfile_shouldReturnProfile() throws Exception {
        when(userProfileService.getUserProfile(42L)).thenReturn(profileDto);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/42/profile"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(42L))
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.avatarUrl").value("avatar.png"))
                .andExpect(jsonPath("$.bio").value("Bio"));

        verify(userProfileService).getUserProfile(42L);
    }

    @Test
    void getProfile_shouldReturn404WhenNotFound() throws Exception {
        when(userProfileService.getUserProfile(42L)).thenThrow(new UserProfileNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.get("/users/42/profile"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateProfile_shouldReturnUpdatedProfile() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile(
                "avatarImage", "avatar.png", "image/png", "data".getBytes()
        );

        UpdateUserProfileRequest request = new UpdateUserProfileRequest();
        request.setUsername("newUser");
        request.setBio("New bio");
        request.setAvatarImage(avatar);

        when(userProfileService.updateProfile(eq(42L), any(UpdateUserProfileRequest.class)))
                .thenReturn(profileDto);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/42/profile")
                        .file(avatar)
                        .param("username", "newUser")
                        .param("bio", "New bio")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; }) // multipart + PUT
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L));

        verify(userProfileService).updateProfile(eq(42L), any(UpdateUserProfileRequest.class));
    }

    @Test
    void updateProfile_shouldReturn400OnDuplicateUsername() throws Exception {
        when(userProfileService.updateProfile(eq(42L), any(UpdateUserProfileRequest.class)))
                .thenThrow(new DuplicateUsernameException());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/42/profile")
                        .param("username", "duplicate")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateProfile_shouldReturn404OnUserNotFound() throws Exception {
        when(userProfileService.updateProfile(eq(42L), any(UpdateUserProfileRequest.class)))
                .thenThrow(new UserNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/42/profile")
                        .param("username", "anyuser")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateProfile_shouldReturn404OnProfileNotFound() throws Exception {
        when(userProfileService.updateProfile(eq(42L), any(UpdateUserProfileRequest.class)))
                .thenThrow(new UserProfileNotFoundException());

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/42/profile")
                        .param("username", "anyuser")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").exists());
    }

    @Test
    void updateProfile_shouldReturn400OnIOException() throws Exception {
        when(userProfileService.updateProfile(eq(42L), any(UpdateUserProfileRequest.class)))
                .thenThrow(IOException.class);

        mockMvc.perform(MockMvcRequestBuilders.multipart("/users/42/profile")
                        .param("username", "anyuser")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Unable to upload file"));
    }
}
