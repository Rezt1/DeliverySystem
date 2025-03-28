package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "deliveryGuySalaries")
public class DeliveryGuySalary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private double amount;

    @Column(nullable = false)
    private LocalDateTime startDate;

    @Column(nullable = false)
    private LocalDateTime endDate;

    @Column
    private boolean isBonus;                // ADDITIONAL FIELD -> IF WE DON'T NEED IT IN BONUS IMPL DELETE IT!!!

    @ManyToOne
    @JoinColumn(name = "deliveryGuyId", nullable = false)
    private DeliveryGuy deliveryGuy;
}
