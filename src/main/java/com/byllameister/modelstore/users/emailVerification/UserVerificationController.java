package com.byllameister.modelstore.users.emailVerification;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users/verification")
@AllArgsConstructor
public class UserVerificationController {
    private final UserVerificationService userVerificationService;

    @PostMapping
    public void sendVerificationEmail() {
        userVerificationService.sendVerificationEmail();
    }

    @GetMapping("/{verificationToken}")
    public void verify(@PathVariable @NotBlank UUID verificationToken) {
        userVerificationService.verify(verificationToken);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(VerificationTokenNotFoundException.class)
    public ResponseEntity<ErrorDto> handleVerificationTokenNotFoundException(VerificationTokenNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(VerificationTokenExpiredException.class)
    public ResponseEntity<ErrorDto> handleVerificationTokenExpiredException(VerificationTokenExpiredException ex) {
        return ResponseEntity.badRequest().body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(VerificationForbiddenException.class)
    ResponseEntity<Void> handleVerificationForbiddenException() {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
    }
}


