package com.byllameister.modelstore.users;

import com.byllameister.modelstore.common.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public Page<UserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> createUser(
            @RequestBody @Valid RegisterUserRequest request,
            UriComponentsBuilder uriComponentsBuilder
    ) {
        var user = userService.createUser(request);
        var uri = uriComponentsBuilder.path("/users/{id}").
                buildAndExpand(user.getId()).
                toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me() {
        var user = userService.getCurrentUser();
        return ResponseEntity.ok().body(user);
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
