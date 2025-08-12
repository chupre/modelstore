package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.products.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @JoinColumn(name = "is_selected")
    private boolean isSelected;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    public BigDecimal getPrice() {
        return product.getPrice();
    }
}