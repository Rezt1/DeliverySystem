package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "cities")
public class City {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private double salary;

    @Column(nullable = false)
    private boolean isDeleted;

    @OneToMany(mappedBy = "location")
    List<User> users = new ArrayList<>();

    @OneToMany(mappedBy = "city")
    List<Restaurant> restaurants = new ArrayList<>();

    @OneToMany(mappedBy = "workCity")
    List<DeliveryGuy> deliveryGuys = new ArrayList<>();
}
