package com.renascence.backend.entities;

import com.renascence.backend.enums.FoodCategory;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Table (name = "foods")
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double price;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FoodCategory category;

    @ManyToOne
    @JoinColumn(name = "cuisineId", nullable = false)
    private Cuisine cuisine;

    @ManyToOne
    @JoinColumn(name = "restaurantId", nullable = false)
    private Restaurant restaurant;

    @OneToMany(mappedBy = "food")
    private List<DeliveryFood> deliveriesFoods = new ArrayList<>();
}
