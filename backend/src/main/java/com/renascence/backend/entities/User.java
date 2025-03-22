package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String phoneNumber;

    @ManyToOne
    @JoinColumn(name = "locationId", nullable = false)
    private City location;

    @OneToOne(mappedBy = "user")
    private DeliveryGuy deliveryGuy;

    @ManyToMany
    @JoinTable(
            name = "usersRoles",
            joinColumns = @JoinColumn(name = "userId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    private Set<Role> roles = new HashSet<>();

}