package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.DuplicateUsernameException;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users/{id}/profile")
@AllArgsConstructor
public class UserProfileController {
    private UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileDto> getProfile(@PathVariable Long id) {
        var profile = userProfileService.getUserProfile(id);
        return ResponseEntity.ok(profile);
    }

    @PreAuthorize("@userProfilePermissionEvaluator.hasAccess(#id)")
    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(
            @Valid @ModelAttribute UpdateUserProfileRequest request,
            @PathVariable Long id
    ) throws IOException {
        var updatedProfile = userProfileService.updateProfile(id, request);
        return ResponseEntity.ok(updatedProfile);
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUsernameException(DuplicateUsernameException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserProfileNotFoundException(UserProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ErrorDto> handleIOException() {
        return ResponseEntity.badRequest().body(new ErrorDto("Unable to upload file"));
    }
}
