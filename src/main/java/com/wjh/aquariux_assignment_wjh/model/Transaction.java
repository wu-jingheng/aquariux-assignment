package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "transaction")
public class Transaction {

    public Transaction(User user, String transactionType, String currencySymbol, BigDecimal amount) {
        this.user = user;
        this.transactionType = transactionType;
        this.currencySymbol = currencySymbol;
        this.amount = amount;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "userId", nullable = false, updatable = false)
    private User user;

    @Column(nullable = false, updatable = false)
    private String transactionType; // BUY or SELL

    @Column(nullable = false, updatable = false)
    private String currencySymbol;

    @Column(nullable = false, updatable = false)
    private BigDecimal amount;

    @Column(nullable = false, updatable = false)
    private LocalDateTime executionTimestamp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.executionTimestamp = LocalDateTime.now(ZoneId.of("UTC"));
        this.createdAt = LocalDateTime.now();
    }
}
