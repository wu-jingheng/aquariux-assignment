package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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
    @MapKeyColumn(name = "currency")
    @Column(name = "balance", nullable = false)
    private Map<String, BigDecimal> balances = new HashMap<>();

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void addCurrency(String currencySymbol, BigDecimal amount) {
        this.balances.put(currencySymbol, balances.getOrDefault(currencySymbol, BigDecimal.ZERO).add(amount));
        onUpdate();
    }

    public void deductCurrency(String currencySymbol, BigDecimal amount) {
        this.balances.put(currencySymbol, balances.getOrDefault(currencySymbol, BigDecimal.ZERO).subtract(amount));
        onUpdate();
    }

    public BigDecimal getBalance(String currencySymbol) {
        return this.balances.getOrDefault(currencySymbol, BigDecimal.ZERO);
    }
}
