package com.byllameister.modelstore.users.passwordReset;

import com.byllameister.modelstore.auth.JwtService;
import com.byllameister.modelstore.email.EmailService;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PasswordResetController.class)
@ActiveProfiles("test")
@AutoConfigureMockMvc(addFilters = false)
class PasswordResetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PasswordResetService passwordResetService;
    
    @MockitoBean
    private UserRepository userRepository;
    
    @MockitoBean
    private EmailService emailService;
    
    @MockitoBean
    private JwtService jwtService;

    // --- POST /users/passwordReset ---
    @Test
    void sendPasswordResetEmail_shouldSendEmailAndReturnOk() throws Exception {
        mockMvc.perform(post("/users/passwordReset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "user@example.com"
                        }
                        """))
                .andExpect(status().isOk());

        verify(passwordResetService).sendPasswordResetEmail(any(sendPasswordResetEmailRequest.class));
    }

    // --- GET /users/passwordReset ---
    @Test
    void validateToken_shouldReturnTokenResponse() throws Exception {
        PasswordResetTokenResponse response = new PasswordResetTokenResponse(1L, 42L, "token-hash", Instant.now());
        
        when(passwordResetService.validate("valid-token")).thenReturn(response);

        mockMvc.perform(get("/users/passwordReset")
                        .param("token", "valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.userId").value(42L));

        verify(passwordResetService).validate("valid-token");
    }

    // --- POST /users/passwordReset/confirm ---
    @Test
    void confirm_shouldChangePasswordAndReturnOk() throws Exception {
        mockMvc.perform(post("/users/passwordReset/confirm")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "token": "valid-token",
                          "newPassword": "NewPassword1"
                        }
                        """))
                .andExpect(status().isOk());

        verify(passwordResetService).changePassword(any(PasswordResetRequest.class));
    }

    // --- Exception handlers ---
    @Test
    void handleUserNotFoundException_shouldReturn404() throws Exception {
        doThrow(UserNotFoundException.class).when(passwordResetService).sendPasswordResetEmail(any());

        mockMvc.perform(post("/users/passwordReset")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "email": "nonexistent@example.com"
                        }
                        """))
                .andExpect(status().isNotFound());
    }

    @Test
    void handlePasswordResetTokenNotFoundException_shouldReturn404() throws Exception {
        when(passwordResetService.validate("invalid-token")).thenThrow(PasswordResetTokenNotFoundException.class);

        mockMvc.perform(get("/users/passwordReset")
                        .param("token", "invalid-token"))
                .andExpect(status().isNotFound());
    }

    @Test
    void handlePasswordResetTokenExpiredException_shouldReturn400() throws Exception {
        when(passwordResetService.validate("expired-token")).thenThrow(PasswordResetTokenExpiredException.class);

        mockMvc.perform(get("/users/passwordReset")
                        .param("token", "expired-token"))
                .andExpect(status().isBadRequest());
    }
}