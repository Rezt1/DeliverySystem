package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Table(name = "deliveryGuys")
public class DeliveryGuy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;

    @Column(nullable = true, length = 50, unique = true)   // CONVERT TO FALSE IF IT WORKS
    private String iban;

    @Column(nullable = false)
    private LocalDate startWorkDate;

    private LocalDate endWorkDate;

    @Column(nullable = false)
    private boolean isFired;

    @ManyToOne
    @JoinColumn(name = "workCityId", nullable = false)
    private City workCity;

    @OneToMany(mappedBy = "deliveryGuy")
    List<DeliveryGuySalary> salaries = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryGuy")
    List<Delivery> deliveries = new ArrayList<>();
}