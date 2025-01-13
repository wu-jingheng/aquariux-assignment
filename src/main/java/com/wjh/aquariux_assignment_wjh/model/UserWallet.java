package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
@Table(name = "user_wallet")
public class UserWallet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @ElementCollection
    @CollectionTable(name = "wallet_balances", joinColumns = @JoinColumn(name = "walletId"))
    @MapKeyColumn(name = "ticker_symbol")
    @Column(name = "balance", nullable = false)
    private Map<String, BigDecimal> balances = new HashMap<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addCurrency(String tickerSymbol, BigDecimal amount) {
        this.balances.put(tickerSymbol, balances.getOrDefault(tickerSymbol, BigDecimal.ZERO).add(amount));
        onUpdate();
    }

    public void deductCurrency(String tickerSymbol, BigDecimal amount) {
        this.balances.put(tickerSymbol, balances.getOrDefault(tickerSymbol, BigDecimal.ZERO).subtract(amount));
        onUpdate();
    }

    public BigDecimal getBalance(String tickerSymbol) {
        return this.balances.getOrDefault(tickerSymbol, BigDecimal.ZERO);
    }

    @PrePersist
    public void onCreate() {
        LocalDateTime now = LocalDateTime.now();
        this.createdAt = now;
        this.updatedAt = now;
    }
}
