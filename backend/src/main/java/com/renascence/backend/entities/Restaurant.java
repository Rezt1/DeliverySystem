package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "restaurants")
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private float rating;

    @Column(nullable = false)
    private double deliveryGuySalary;

    @Column(nullable = false, length = 50, unique = true)
    private String iban;

    @ManyToOne
    @JoinColumn(name = "cityId", nullable = false)
    private City city;

    @ManyToOne
    @JoinColumn(name = "ownerId", nullable = false)
    private User owner;

    @OneToOne
    @JoinColumn(name = "bonusId", nullable = false)
    private Bonus bonus;

    @OneToMany(mappedBy = "restaurant")
    private List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "workPlace")
    private List<DeliveryGuy> deliveryGuys = new ArrayList<>();

    @OneToMany(mappedBy = "restaurant")
    private List<Food> foods = new ArrayList<>();
}
