package com.renascence.backend.entities;

import com.renascence.backend.enums.DeliveryStatus;
import com.renascence.backend.enums.PaymentMethod;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name = "deliveries")
public class Delivery {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "receiverId", nullable = false)
    private User receiver;

    @ManyToOne
    @JoinColumn(name = "deliveryGuyId", nullable = false)
    private DeliveryGuy deliveryGuy;

    @ManyToOne
    @JoinColumn(name = "restaurantId", nullable = false)
    private Restaurant restaurant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DeliveryStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private Date date;
}
