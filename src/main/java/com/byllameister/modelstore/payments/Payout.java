package com.byllameister.modelstore.payments;

import com.byllameister.modelstore.orders.Order;
import com.byllameister.modelstore.sellers.Seller;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "payouts")
public class Payout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "seller_id")
    private Seller seller;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "status", length = 20)
    @Enumerated(EnumType.STRING)
    private PayoutStatus status;

    @Column(name = "payment_id")
    private String paymentId;

    @ColumnDefault("now()")
    @Column(name = "created_at")
    private Instant createdAt;
}