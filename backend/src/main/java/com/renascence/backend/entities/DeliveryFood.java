package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "deliveriesFoods")
public class DeliveryFood {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "deliveryId", nullable = false)
    private Delivery delivery;

    @ManyToOne
    @JoinColumn(name = "foodId", nullable = false)
    private Food food;

    @Column(nullable = false)
    private int foodCount;
}
