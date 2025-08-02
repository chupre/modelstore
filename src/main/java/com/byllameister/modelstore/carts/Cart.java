package com.byllameister.modelstore.carts;

import com.byllameister.modelstore.products.Product;
import com.byllameister.modelstore.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "carts")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", insertable = false, updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL,  orphanRemoval = true)
    private Set<CartItem> cartItems = new LinkedHashSet<>();

    public CartItem addItem(Product product) {
        var cartItem = getItem(product);
        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setCart(this);
            cartItems.add(cartItem);
        }

        return cartItem;
    }

    public CartItem getItem(Product product) {
        for (CartItem cartItem : cartItems) {
            if (cartItem.getProduct().equals(product)) {
                return cartItem;
            }
        }
        return null;
    }

    public BigDecimal getTotalPrice() {
        return cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}