package com.byllameister.modelstore.admin.users;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.users.*;
import com.byllameister.modelstore.users.profiles.UpdateUserProfileRequest;
import com.byllameister.modelstore.users.profiles.UserProfileDto;
import com.byllameister.modelstore.users.profiles.UserProfileService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/admin/users")
@AllArgsConstructor
public class AdminUserController {
    UserService userService;
    UserProfileService userProfileService;

    @GetMapping
    public ResponseEntity<Page<UserExposedResponse>> getAllUsers(Pageable pageable) {
        var users = userService.getAllUsersExposed(pageable);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserExposedResponse> getUserById(@PathVariable Long id) {
        var user = userService.getUserExposedById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserExposedResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {
        var user = userService.updateUser(id, request);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserProfileDto> updateUserProfile(
            @PathVariable Long id,
            @Valid @ModelAttribute UpdateUserProfileRequest request
    ) throws IOException {
        var updatedProfile = userProfileService.updateProfile(id, request);
        return ResponseEntity.ok(updatedProfile);
    }


    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorDto> handleDuplicateEmail(DuplicateEmailException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(DuplicateUsernameException.class)
    public ResponseEntity<ErrorDto> handleDuplicateUsername(DuplicateUsernameException e) {
        return ResponseEntity.badRequest().body(new ErrorDto(e.getMessage()));
    }
}
