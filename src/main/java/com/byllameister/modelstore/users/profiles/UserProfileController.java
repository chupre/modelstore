package com.byllameister.modelstore.users.profiles;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.User;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/users/me/profile")
@AllArgsConstructor
public class UserProfileController {
    private UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<UserProfileDto> getCurrentProfile() {
        var profile = userProfileService.getCurrentUserProfile();
        return ResponseEntity.ok(profile);
    }

    @PutMapping
    public ResponseEntity<UserProfileDto> updateProfile(
            @Valid @ModelAttribute UpdateUserProfileRequest request
    ) throws IOException {
        var updatedProfile = userProfileService.updateProfile(User.getCurrentUserId(), request);
        return ResponseEntity.ok(updatedProfile);
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
