package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(nullable = false, length = 50, unique = true)
    private String iban;

    @ManyToOne
    @JoinColumn(name = "workPlaceId")
    private Restaurant workPlace;

    @OneToMany(mappedBy = "deliveryGuy")
    List<DeliveryGuySalary> salaries = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryGuy")
    List<Delivery> deliveries = new ArrayList<>();

    @OneToMany(mappedBy = "deliveryGuy")
    List<Contract> contracts = new ArrayList<>();
}