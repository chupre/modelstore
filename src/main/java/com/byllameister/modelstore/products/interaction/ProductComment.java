package com.byllameister.modelstore.products.interaction;

import com.byllameister.modelstore.products.Product;
import com.byllameister.modelstore.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "product_comments")
public class ProductComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "comment", length = 1000)
    private String comment;

    @OneToMany(mappedBy = "comment", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ProductCommentLike> likes = new ArrayList<>();

    @Column(name = "created_at", insertable = false, updatable = false)
    private Instant createdAt = Instant.now();
}