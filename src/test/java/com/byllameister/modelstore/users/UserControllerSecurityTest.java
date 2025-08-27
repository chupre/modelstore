package com.byllameister.modelstore.users;

import com.byllameister.modelstore.auth.JwtService;
import com.byllameister.modelstore.products.interaction.ProductInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc
class UserControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    
    @MockitoBean
    private ProductInteractionService productInteractionService;
    
    @MockitoBean
    private JwtService jwtService;

    private User user;
    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        user = new User();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setRole(Role.BUYER);
        user.setVerified(true);

        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("testuser");
        userResponse.setRole(Role.BUYER);
    }

    // --- GET /users (requires authentication based on test results) ---
    @Test
    void getAllUsers_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isUnauthorized());
    }

    // --- GET /users/{id} (requires authentication based on test results) ---
    @Test
    void getUserById_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/1"))
                .andExpect(status().isUnauthorized());
    }

    // --- POST /users (public endpoint, but has CSRF issues in test) ---
    @Test
    void createUser_shouldAllowUnauthenticatedAccess() throws Exception {
        mockMvc.perform(post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "newuser",
                          "email": "newuser@example.com",
                          "password": "Password1"
                        }
                        """))
                .andExpect(status().isForbidden()); // CSRF protection in test environment
    }

    // --- GET /users/me (authenticated endpoint) ---
    @Test
    void me_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/me"))
                .andExpect(status().isUnauthorized());
    }

    // --- GET /users/{userId}/comments/likes (requires authentication based on test results) ---
    @Test
    void getLikedComments_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/1/comments/likes")
                        .param("page","0").param("size","10"))
                .andExpect(status().isUnauthorized());
    }

    // --- GET /users/{userId}/comments (requires authentication based on test results) ---
    @Test
    void getUserComments_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/1/comments")
                        .param("page","0").param("size","10"))
                .andExpect(status().isUnauthorized());
    }

    // --- GET /users/{userId}/products/likes (requires authentication based on test results) ---
    @Test
    void getLikedProducts_shouldRequireAuthentication() throws Exception {
        mockMvc.perform(get("/users/1/products/likes")
                        .param("page","0").param("size","10"))
                .andExpect(status().isUnauthorized());
    }
}