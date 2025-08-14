package com.byllameister.modelstore.sellers;

import com.byllameister.modelstore.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "sellers")
public class Seller {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "payout_method", length = 20)
    @Enumerated(EnumType.STRING)
    private PayoutMethod payoutMethod;

    @Column(name = "payout_destination", length = 64)
    private String payoutDestination;
}