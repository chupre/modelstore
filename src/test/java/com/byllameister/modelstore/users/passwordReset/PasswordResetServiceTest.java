package com.byllameister.modelstore.users.passwordReset;

import com.byllameister.modelstore.email.Email;
import com.byllameister.modelstore.email.EmailFactory;
import com.byllameister.modelstore.email.EmailService;
import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.users.UserNotFoundException;
import com.byllameister.modelstore.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class PasswordResetServiceTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private EmailService emailService;
    
    @Mock
    private EmailFactory emailFactory;
    
    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;
    
    @Mock
    private PasswordResetTokenMapper passwordResetTokenMapper;
    
    @Mock
    private PasswordEncoder passwordEncoder;
    
    @InjectMocks
    private PasswordResetService passwordResetService;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Set the durationMinutes field using reflection since it's injected via @Value
        try {
            java.lang.reflect.Field field = PasswordResetService.class.getDeclaredField("durationMinutes");
            field.setAccessible(true);
            field.set(passwordResetService, 30L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    
    @Test
    void sendPasswordResetEmail_shouldThrowUserNotFoundException_whenUserNotFound() {
        sendPasswordResetEmailRequest request = new sendPasswordResetEmailRequest("test@example.com");
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());
        
        assertThrows(UserNotFoundException.class, () -> passwordResetService.sendPasswordResetEmail(request));
        
        verify(userRepository).findByEmail("test@example.com");
        verifyNoMoreInteractions(userRepository);
    }
    
    @Test
    void sendPasswordResetEmail_shouldCreateTokenAndSendEmail() {
        // Arrange
        sendPasswordResetEmailRequest request = new sendPasswordResetEmailRequest("test@example.com");
        User user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        
        String rawToken = "rawToken123";
        String hashToken = "hashedToken123";
        
        // Mock the static method in PasswordResetTokenUtils
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(PasswordResetTokenUtils::generateRawToken).thenReturn(rawToken);
            utilities.when(() -> PasswordResetTokenUtils.hashToken(rawToken)).thenReturn(hashToken);
            
            PasswordResetToken token = new PasswordResetToken();
            token.setUser(user);
            token.setTokenHash(hashToken);
            token.setExpiresAt(Instant.now().plus(Duration.ofMinutes(30)));
            
            Email email = new Email("test@example.com", "Reset Password", "Reset your password");
            
            when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(invocation -> invocation.getArgument(0));
            when(emailFactory.createPasswordResetEmail("test@example.com", rawToken)).thenReturn(email);
            
            // Act
            passwordResetService.sendPasswordResetEmail(request);
            
            // Assert
            verify(userRepository).findByEmail("test@example.com");
            verify(passwordResetTokenRepository).save(any(PasswordResetToken.class));
            verify(emailFactory).createPasswordResetEmail("test@example.com", rawToken);
            verify(emailService).sendSimpleEmail(email);
        }
    }
    
    @Test
    void validate_shouldThrowPasswordResetTokenNotFoundException_whenTokenNotFound() {
        String rawToken = "rawToken123";
        String hashToken = "hashedToken123";
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken(rawToken)).thenReturn(hashToken);
            
            when(passwordResetTokenRepository.findByTokenHash(hashToken)).thenReturn(Optional.empty());
            
            assertThrows(PasswordResetTokenNotFoundException.class, () -> passwordResetService.validate(rawToken));
        }
        
        verify(passwordResetTokenRepository).findByTokenHash(hashToken);
    }
    
    @Test
    void validate_shouldThrowPasswordResetTokenExpiredException_whenTokenIsExpired() {
        String rawToken = "rawToken123";
        String hashToken = "hashedToken123";
        
        PasswordResetToken token = new PasswordResetToken();
        token.setId(1L);
        token.setTokenHash(hashToken);
        token.setExpiresAt(Instant.now().minusSeconds(1)); // Expired token
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken(rawToken)).thenReturn(hashToken);
            
            when(passwordResetTokenRepository.findByTokenHash(hashToken)).thenReturn(Optional.of(token));
            
            assertThrows(PasswordResetTokenExpiredException.class, () -> passwordResetService.validate(rawToken));
        }
        
        verify(passwordResetTokenRepository).findByTokenHash(hashToken);
    }
    
    @Test
    void validate_shouldReturnTokenResponse_whenTokenIsValid() {
        String rawToken = "rawToken123";
        String hashToken = "hashedToken123";
        
        User user = new User();
        user.setId(1L);
        
        PasswordResetToken token = new PasswordResetToken();
        token.setId(1L);
        token.setUser(user);
        token.setTokenHash(hashToken);
        token.setExpiresAt(Instant.now().plusSeconds(3600)); // Valid token
        
        PasswordResetTokenResponse response = new PasswordResetTokenResponse(1L, 1L, hashToken, token.getExpiresAt());
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken(rawToken)).thenReturn(hashToken);
            
            when(passwordResetTokenRepository.findByTokenHash(hashToken)).thenReturn(Optional.of(token));
            when(passwordResetTokenMapper.toDto(token)).thenReturn(response);
            
            PasswordResetTokenResponse result = passwordResetService.validate(rawToken);
            
            assertThat(result).isEqualTo(response);
        }
        
        verify(passwordResetTokenRepository).findByTokenHash(hashToken);
        verify(passwordResetTokenMapper).toDto(token);
    }
    
    @Test
    void changePassword_shouldThrowPasswordResetTokenNotFoundException_whenTokenIsInvalid() {
        PasswordResetRequest request = new PasswordResetRequest("rawToken123", "NewPassword1");
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken("rawToken123")).thenReturn("hashedToken123");
            
            when(passwordResetTokenRepository.findByTokenHash("hashedToken123")).thenReturn(Optional.empty());
            
            assertThrows(PasswordResetTokenNotFoundException.class, () -> passwordResetService.changePassword(request));
        }
    }
    
    @Test
    void changePassword_shouldThrowUserNotFoundException_whenUserNotFound() {
        PasswordResetRequest request = new PasswordResetRequest("rawToken123", "NewPassword1");
        
        User user = new User();
        user.setId(1L);
        
        PasswordResetToken token = new PasswordResetToken();
        token.setId(1L);
        token.setUser(user);
        token.setTokenHash("hashedToken123");
        token.setExpiresAt(Instant.now().plusSeconds(3600)); // Valid token
        
        PasswordResetTokenResponse response = new PasswordResetTokenResponse(1L, 1L, "hashedToken123", token.getExpiresAt());
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken("rawToken123")).thenReturn("hashedToken123");
            
            when(passwordResetTokenRepository.findByTokenHash("hashedToken123")).thenReturn(Optional.of(token));
            when(passwordResetTokenMapper.toDto(token)).thenReturn(response);
            when(userRepository.findById(1L)).thenReturn(Optional.empty());
            
            assertThrows(UserNotFoundException.class, () -> passwordResetService.changePassword(request));
        }
    }
    
    @Test
    void changePassword_shouldUpdatePasswordAndDeleteToken() {
        PasswordResetRequest request = new PasswordResetRequest("rawToken123", "NewPassword1");
        
        User user = new User();
        user.setId(1L);
        user.setPassword("oldPassword");
        user.setVerified(false);
        
        PasswordResetToken token = new PasswordResetToken();
        token.setId(1L);
        token.setUser(user);
        token.setTokenHash("hashedToken123");
        token.setExpiresAt(Instant.now().plusSeconds(3600)); // Valid token
        
        PasswordResetTokenResponse response = new PasswordResetTokenResponse(1L, 1L, "hashedToken123", token.getExpiresAt());
        
        try (MockedStatic<PasswordResetTokenUtils> utilities = mockStatic(PasswordResetTokenUtils.class)) {
            utilities.when(() -> PasswordResetTokenUtils.hashToken("rawToken123")).thenReturn("hashedToken123");
            
            when(passwordResetTokenRepository.findByTokenHash("hashedToken123")).thenReturn(Optional.of(token));
            when(passwordResetTokenMapper.toDto(token)).thenReturn(response);
            when(userRepository.findById(1L)).thenReturn(Optional.of(user));
            when(passwordEncoder.encode("NewPassword1")).thenReturn("encodedNewPassword");
            
            passwordResetService.changePassword(request);
            
            assertThat(user.getPassword()).isEqualTo("encodedNewPassword");
            assertThat(user.getVerified()).isTrue();
            verify(userRepository).save(user);
            verify(passwordResetTokenRepository).deleteById(1L);
        }
    }
}