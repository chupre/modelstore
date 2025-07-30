package com.byllameister.modelstore.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<Void> handleUserNotFound() {
        return ResponseEntity.notFound().build();
    }
}
