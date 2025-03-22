package com.renascence.backend.entities;

import com.renascence.backend.enums.FoodCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table (name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;

    @ManyToOne
    @JoinColumn(name = "cuisineId")
    private Cuisine cuisine;

    @ManyToOne
    @JoinColumn(name = "restaurantId")
    private Restaurant restaurant;
}
