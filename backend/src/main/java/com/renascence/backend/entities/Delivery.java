package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.renascence.backend.enums.DeliveryStatus;

import java.util.Date;

@Entity
@Setter
@Getter
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "deliveryguy_id", nullable = false)
    private User deliveryGuy;

    @ManyToOne
    @JoinColumn(name = "food_id", nullable = false)
    private Food food;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String address;
    private Date date;
}

