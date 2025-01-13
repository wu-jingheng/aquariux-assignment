package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "aggregated_price")
public class AggregatedPrice {

    public AggregatedPrice() {
    }

    public AggregatedPrice(String tickerSymbol, LocalDateTime aggregateTimestamp) {
        this.tickerSymbol = tickerSymbol;
        this.aggregateTimestamp = aggregateTimestamp;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private String tickerSymbol;

    @Column(nullable = false, updatable = false)
    private LocalDateTime aggregateTimestamp;

    @Column(nullable = false, updatable = false)
    private BigDecimal bidPrice = BigDecimal.ZERO;

    @Column(nullable = false, updatable = false)
    private BigDecimal bidQuantity;

    @Column(nullable = false, updatable = false)
    private String bidExchangePlatform;

    @Column(nullable = false, updatable = false)
    private BigDecimal askPrice = BigDecimal.valueOf(Long.MAX_VALUE);

    @Column(nullable = false, updatable = false)
    private BigDecimal askQuantity;

    @Column(nullable = false, updatable = false)
    private String askExchangePlatform;

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

}
