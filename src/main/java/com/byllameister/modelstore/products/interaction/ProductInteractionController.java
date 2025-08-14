package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.common.ErrorDto;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.users.UserNotFoundException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/interactions/products")
@AllArgsConstructor
public class ProductInteractionController {
    private final ProductInteractionService productInteractionService;

    @GetMapping("/{id}/likes")
    public Long getLikesCount(@PathVariable Long id) {
        return productInteractionService.getLikesCount(id);
    }

    @GetMapping("/{id}/comments")
    public Page<ProductCommentDto> getComments(@PathVariable Long id, Pageable pageable) {
        return productInteractionService.getComments(id, pageable);
    }

    @PostMapping("/{id}/likes")
    public ResponseEntity<ProductLikeDto> like(@PathVariable Long id) {
        var like = productInteractionService.like(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/{id}/likes")
    public ResponseEntity<Void> unlike(@PathVariable Long id) {
        productInteractionService.unlike(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<ProductCommentDto> comment(
            @PathVariable Long id,
            @Valid @RequestBody CommentRequest request) {
        var comment = productInteractionService.comment(id, request.getComment());
        return ResponseEntity.status(HttpStatus.CREATED).body(comment);
    }

    @PreAuthorize("@productCommentPermissionEvaluator.hasAccessToProductComment(#commentId)")
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        productInteractionService.deleteComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("@productCommentPermissionEvaluator.hasAccessToProductComment(#commentId)")
    @PutMapping("/comments/{commentId}")
    public ResponseEntity<ProductCommentDto> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request) {
        var comment = productInteractionService.updateComment(commentId, request.getComment());
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/comments/{commentId}")
    public ResponseEntity<ProductCommentDto> getComment(@PathVariable Long commentId) {
        var comment = productInteractionService.getComment(commentId);
        return ResponseEntity.ok(comment);
    }

    @GetMapping("/comments/{commentId}/likes")
    public Long getLikesCountFromComment(@PathVariable Long commentId) {
        return productInteractionService.getLikesCountFromComment(commentId);
    }

    @PostMapping("/comments/{commentId}/likes")
    public ResponseEntity<ProductCommentLikeDto> likeComment(@PathVariable Long commentId) {
        var like = productInteractionService.likeComment(commentId);
        return ResponseEntity.status(HttpStatus.CREATED).body(like);
    }

    @DeleteMapping("/comments/{commentId}/likes")
    public ResponseEntity<Void> unlikeComment(@PathVariable Long commentId) {
        productInteractionService.unlikeComment(commentId);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(ProductLikeAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleProductLikeAlreadyExistsException(ProductLikeAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(ProductCommentLikeAlreadyExistsException.class)
    public ResponseEntity<ErrorDto> handleProductCommentLikeAlreadyExistsException(ProductCommentLikeAlreadyExistsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(ProductCommentNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductCommentNotFoundException(ProductCommentNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorDto> handleProductNotFoundException(ProductNotFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }
}
