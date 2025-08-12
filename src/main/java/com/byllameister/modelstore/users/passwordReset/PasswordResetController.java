package com.byllameister.modelstore.users.passwordReset;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users/passwordReset")
@AllArgsConstructor
public class PasswordResetController {
    private final PasswordResetService passwordResetService;

    @PostMapping
    public void sendPasswordResetEmail(
            @Valid @RequestBody sendPasswordResetEmailRequest request
    ) {
        passwordResetService.sendPasswordResetEmail(request);
    }

    @GetMapping
    public ResponseEntity<PasswordResetTokenResponse> validateToken(@RequestParam(name = "token") @NotBlank String rawToken) {
        var token = passwordResetService.validate(rawToken);
        return ResponseEntity.ok(token);
    }

    @PostMapping("/confirm")
    public void confirm(
        @Valid @RequestBody PasswordResetRequest request
    ) {
        passwordResetService.changePassword(request);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordResetTokenNotFoundException.class)
    public ResponseEntity<ErrorDto> handlePasswordResetTokenNotFoundException(PasswordResetTokenNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(PasswordResetTokenExpiredException.class)
    public ResponseEntity<ErrorDto> handlePasswordResetTokenExpiredException(PasswordResetTokenExpiredException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }
}
