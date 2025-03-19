package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Entity
@Setter
@Getter
public class Restaurant {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long restaurant_id;

        private String name;
        private String address;

        @ManyToOne
        @JoinColumn(name = "user_id", nullable = false)
        private User owner;

}
