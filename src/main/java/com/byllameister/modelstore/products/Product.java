package com.byllameister.modelstore.products;

import com.byllameister.modelstore.users.User;
import com.byllameister.modelstore.categories.Category;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description", length = Integer.MAX_VALUE)
    private String description;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "previewimage")
    private String previewImage;

    @Column(name = "file")
    private String file;

    @ManyToOne()
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne()
    @JoinColumn(name = "category_id")
    private Category category;

    @Column(name = "createdat", updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;
}