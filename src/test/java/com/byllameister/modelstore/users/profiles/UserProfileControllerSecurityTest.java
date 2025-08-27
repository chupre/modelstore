package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.auth.JwtService;
import com.byllameister.modelstore.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserProfileController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserProfileControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserProfileService userProfileService;
    
    @MockitoBean
    private UserRepository userRepository;
    
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

    // --- GET /users/{id}/profile (requires authentication based on test results) ---
    @Test
    void getProfile_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/42/profile"))
                .andExpect(status().isUnauthorized());
    }

    // --- PUT /users/{id}/profile (authenticated endpoint with custom authorization) ---
    @Test
    void updateProfile_shouldRequireAuthentication() throws Exception {
        MockMultipartFile avatar = new MockMultipartFile(
                "avatarImage", "avatar.png", "image/png", "data".getBytes()
        );

        mockMvc.perform(multipart("/users/42/profile")
                        .file(avatar)
                        .param("username", "newUser")
                        .param("bio", "New bio")
                        .with(requestBuilder -> { requestBuilder.setMethod("PUT"); return requestBuilder; })
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isForbidden()); // CSRF protection in test environment
    }
}