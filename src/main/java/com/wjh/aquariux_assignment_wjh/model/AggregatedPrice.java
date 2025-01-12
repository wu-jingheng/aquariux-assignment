package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "aggregated_price")
public class AggregatedPrice {

    // TODO: Custom constructor

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String currencySymbol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime aggregateTimestamp;

    @Column(nullable = false, updatable = false)
    private BigDecimal bidPrice;

    @Column(nullable = false, updatable = false)
    private BigDecimal bidQuantity;

    @Column(nullable = false, updatable = false)
    private String bidExchangePlatform;

    @Column(nullable = false, updatable = false)
    private BigDecimal askPrice;

    @Column(nullable = false, updatable = false)
    private BigDecimal askQuantity;

    @Column(nullable = false, updatable = false)
    private String askExchangePlatform;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.aggregateTimestamp = LocalDateTime.now(ZoneId.of("UTC"));
        this.createdAt = LocalDateTime.now();
    }
}
