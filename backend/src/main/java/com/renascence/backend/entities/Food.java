package com.renascence.backend.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import com.renascence.backend.enums.FoodType;

@Entity
@Setter
@Getter
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long food_id;

    private String name;
    private Double price;

    @Enumerated(EnumType.STRING)
    private FoodType type;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
}
