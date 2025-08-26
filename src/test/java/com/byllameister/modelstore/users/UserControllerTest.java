package com.byllameister.modelstore.users;

import com.byllameister.modelstore.auth.JwtService;
import com.byllameister.modelstore.products.ProductWithLikesResponse;
import com.byllameister.modelstore.products.interaction.ProductCommentDto;
import com.byllameister.modelstore.products.interaction.ProductInteractionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private UserService userService;
    @MockitoBean
    private ProductInteractionService productInteractionService;
    @MockitoBean
    private JwtService jwtService;

    private UserResponse userResponse;

    @BeforeEach
    void setup() {
        userResponse = new UserResponse();
        userResponse.setId(1L);
        userResponse.setUsername("user");
        userResponse.setRole(Role.BUYER);
    }

    // --- GET /users ---
    @Test
    void getAllUsers_shouldReturnPage() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        Page<UserResponse> page = new PageImpl<>(List.of(userResponse));
        when(userService.getAllUsers(pageable)).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].id").value(userResponse.getId()))
                .andExpect(jsonPath("$.content[0].username").value(userResponse.getUsername()));
    }

    // --- GET /users/{id} ---
    @Test
    void getUserById_shouldReturnUser() throws Exception {
        when(userService.getUserById(1L)).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()));
    }

    // --- POST /users ---
    @Test
    void createUser_shouldReturnCreated() throws Exception {
        RegisterUserRequest request = new RegisterUserRequest(
                "user","test@example.com","Password1"
        );
        when(userService.createUser(any(RegisterUserRequest.class))).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "user",
                          "email": "test@example.com",
                          "password": "Password1"
                        }
                        """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()));
    }

    // --- GET /users/me ---
    @Test
    void me_shouldReturnCurrentUser() throws Exception {
        when(userService.getCurrentUser()).thenReturn(userResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/me"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(userResponse.getId()))
                .andExpect(jsonPath("$.username").value(userResponse.getUsername()));
    }

    // --- GET /users/{userId}/comments/likes ---
    @Test
    void getLikedComments_shouldReturnPage() throws Exception {
        Page<ProductCommentDto> page = new PageImpl<>(List.of());
        when(productInteractionService.getLikedComments(1L, PageRequest.of(0,10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/comments/likes")
                        .param("page","0").param("size","10"))
                .andExpect(status().isOk());
    }

    // --- GET /users/{userId}/comments ---
    @Test
    void getUserComments_shouldReturnPage() throws Exception {
        Page<ProductCommentDto> page = new PageImpl<>(List.of());
        when(productInteractionService.getCommentsByUserId(1L, PageRequest.of(0,10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/comments")
                        .param("page","0").param("size","10"))
                .andExpect(status().isOk());
    }

    // --- GET /users/{userId}/products/likes ---
    @Test
    void getLikedProducts_shouldReturnPage() throws Exception {
        Page<ProductWithLikesResponse> page = new PageImpl<>(List.of());
        when(productInteractionService.getLikedProducts(1L, PageRequest.of(0,10))).thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1/products/likes")
                        .param("page","0").param("size","10"))
                .andExpect(status().isOk());
    }

    // --- Exception handlers ---
    @Test
    void handleUserNotFound_shouldReturn404() throws Exception {
        doThrow(UserNotFoundException.class).when(userService).getUserById(1L);

        mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void handleDuplicateEmail_shouldReturn400() throws Exception {
        doThrow(DuplicateEmailException.class).when(userService).createUser(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "user",
                          "email": "test@example.com",
                          "password": "Password1"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }

    @Test
    void handleDuplicateUsername_shouldReturn400() throws Exception {
        doThrow(DuplicateUsernameException.class).when(userService).createUser(any());

        mockMvc.perform(MockMvcRequestBuilders.post("/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "username": "user",
                          "email": "test@example.com",
                          "password": "Password1"
                        }
                        """))
                .andExpect(status().isBadRequest());
    }
}
