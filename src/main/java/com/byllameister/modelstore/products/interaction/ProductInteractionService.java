package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.common.PageableUtils;
import com.byllameister.modelstore.products.ProductMapper;
import com.byllameister.modelstore.products.ProductNotFoundException;
import com.byllameister.modelstore.products.ProductRepository;
import com.byllameister.modelstore.products.ProductWithLikesResponse;
import com.byllameister.modelstore.users.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductInteractionService {
    private final ProductLikeRepository productLikeRepository;
    private final ProductCommentRepository productCommentRepository;
    private final ProductCommentMapper productCommentMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ProductLikeMapper productLikeMapper;
    private final ProductCommentLikeRepository productCommentLikeRepository;
    private final ProductCommentLikeMapper productCommentLikeMapper;
    private final ProductMapper productMapper;

    public Long getLikesCount(Long id) {
        return productLikeRepository.countAllByProductId(id);
    }

    public Page<ProductCommentDto> getComments(Long id, Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.COMMENT_SORT_FIELDS);
        return productCommentRepository.findAll(id, pageable);
    }

    public Page<CommentWithUserLikeResponse> getCommentsWithUserLike(Long id, Pageable pageable) {
        PageableUtils.validate(pageable, PageableUtils.COMMENT_SORT_FIELDS);
        var userId = User.getCurrentUserId();
        return productCommentRepository.findAllWithUserLike(id, userId, pageable);
    }

    public ProductLikeDto like(Long id) {
        if (productLikeRepository.exists(id, User.getCurrentUserId())) {
            throw new ProductLikeAlreadyExistsException();
        }

        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        var user = userRepository.findById(User.getCurrentUserId()).orElseThrow(UserNotFoundException::new);

        var like = new ProductLike();
        like.setProduct(product);
        like.setUser(user);
        productLikeRepository.save(like);
        return productLikeMapper.toDto(like);
    }

    public void unlike(Long id) {
        productLikeRepository.deleteLike(id, User.getCurrentUserId());
    }

    public ProductCommentDto comment(Long id, String text) {
        var product = productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
        var user = userRepository.findById(User.getCurrentUserId()).orElseThrow(UserNotFoundException::new);

        var comment = new ProductComment();
        comment.setProduct(product);
        comment.setUser(user);
        comment.setComment(text);
        productCommentRepository.save(comment);
        return productCommentMapper.toDto(comment);
    }

    public void deleteComment(Long commentId) {
        productCommentRepository.deleteById(commentId);
    }

    public ProductCommentDto updateComment(Long commentId, @NotBlank String text) {
        var comment = productCommentRepository.findById(commentId).orElseThrow(ProductCommentNotFoundException::new);
        comment.setComment(text);
        productCommentRepository.save(comment);
        return productCommentMapper.toDto(comment);
    }

    public ProductCommentDto getComment(Long commentId) {
        var comment = productCommentRepository.findById(commentId).orElseThrow(ProductCommentNotFoundException::new);
        var likes = productCommentLikeRepository.countByCommentId(commentId);
        var dto = productCommentMapper.toDto(comment);
        dto.setLikes(likes);
        return dto;
    }

    public Long getLikesCountFromComment(Long commentId) {
        return productCommentLikeRepository.countAllByCommentId(commentId);
    }

    public ProductCommentLikeDto likeComment(Long commentId) {
        if (productCommentLikeRepository.exists(commentId, User.getCurrentUserId())) {
            throw new ProductCommentLikeAlreadyExistsException();
        }

        var comment = productCommentRepository.findById(commentId).orElseThrow(ProductCommentNotFoundException::new);
        var user = userRepository.findById(User.getCurrentUserId()).orElseThrow(UserNotFoundException::new);

        var like = new ProductCommentLike();
        like.setComment(comment);
        like.setUser(user);
        productCommentLikeRepository.save(like);
        return productCommentLikeMapper.toDto(like);
    }

    public void unlikeComment(Long commentId) {
        productCommentLikeRepository.deleteLike(commentId, User.getCurrentUserId());
    }

    public Page<ProductWithLikesResponse> getLikedProducts(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        var products = productRepository.findLikedByUserId(userId, pageable);
        return products.map(productMapper::toDtoFromFlatDto);
    }

    public Page<ProductCommentDto> getLikedComments(Long userId, Pageable pageable) {
        userRepository.findById(userId).orElseThrow(UserNotFoundException::new);
        return productCommentRepository.findLikedByUserId(userId, pageable);
    }
}
