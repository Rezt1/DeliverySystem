package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bonuses")
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private float percentage;

    @Column(nullable = false)
    private double neededAmountForBonus;

    @OneToOne(mappedBy = "bonus")
    private Restaurant restaurant;
}
