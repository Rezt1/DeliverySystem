package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private double deliveryGuySalary;

    @Column(nullable = false)
    private String iban;

    @ManyToOne
    @JoinColumn(name = "cityId", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private User owner;

    @OneToOne
    @JoinColumn(name = "bonusId")
    private Bonus bonus;
}
