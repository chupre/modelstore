package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "product_comment_likes")
public class ProductCommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private ProductComment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ColumnDefault("now()")
    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt = Instant.now();
}