package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

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
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column
    private boolean isBonus;                // ADDITIONAL FIELD -> IF WE DON'T NEED IT IN BONUS IMPL DELETE IT!!!

    @ManyToOne
    @JoinColumn(name = "deliveryGuyId", nullable = false)
    private DeliveryGuy deliveryGuy;
}
