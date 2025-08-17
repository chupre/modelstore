package com.byllameister.modelstore.users;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.products.interaction.ProductInteractionService;
import com.byllameister.modelstore.users.profiles.UserProfileNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@AllArgsConstructor
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ProductInteractionService productInteractionService;

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

    @GetMapping("/{userId}/products/{productId}/commentLikes")
    public ResponseEntity<LikedCommentsResponse> getLikedComments(
            @PathVariable("userId") Long userId,
            @PathVariable("productId") Long productId
    ) {
        var comments = productInteractionService.getLikedComments(userId, productId);
        return ResponseEntity.ok(comments);
    }

    @GetMapping("/{userId}/likes")
    public ResponseEntity<LikedProductsResponse> getLikedProducts(@PathVariable("userId") Long userId) {
        var likes = productInteractionService.getLikedProducts(userId);
        return ResponseEntity.ok(likes);
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
    }

    @ExceptionHandler(UserProfileNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserProfileNotFoundException(UserProfileNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(ex.getMessage()));
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
