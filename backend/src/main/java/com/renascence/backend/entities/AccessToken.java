package com.renascence.backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "accessTokens")
public class AccessToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(unique = true, nullable = false)
    private String token;

    @Column(nullable = false)
    private LocalDateTime expiryDate;

    @Column(nullable = false)
    private boolean isRevoked;

    @OneToOne()
    @JoinColumn(name = "userId")
    private User user;

    public AccessToken(){}

    public AccessToken(LocalDateTime expiryDate, String token, User user) {
        this.expiryDate = expiryDate;
        this.token = token;
        this.user = user;
    }
}
