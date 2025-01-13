package com.wjh.aquariux_assignment_wjh.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;


@Setter
@Getter
@Entity
@Table(name = "transaction")
public class Transaction {

    public Transaction() {}

    public Transaction(Long userId,
                       String transactionType) {
        this.userId = userId;
        this.transactionType = transactionType;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String transactionId;

    @Column(nullable = false, updatable = false)
    private Long userId;

    @Column(nullable = false, updatable = false)
    private String transactionType; // BUY or SELL

    @Column(nullable = false, updatable = false)
    private String inboundTickerSymbol;

    @Column(nullable = false, updatable = false)
    private BigDecimal inboundAmount;

    @Column(nullable = false, updatable = false)
    private String outboundTickerSymbol;

    @Column(nullable = false, updatable = false)
    private BigDecimal outboundAmount;

    @Column(nullable = false, updatable = false)
    private BigDecimal rate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime orderTimestamp;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    public void onCreate() {
        this.orderTimestamp = LocalDateTime.now(ZoneId.of("UTC"));
        this.createdAt = LocalDateTime.now();
    }

    public void populate(String inboundTickerSymbol,
                         BigDecimal inboundAmount,
                         String outboundTickerSymbol,
                         BigDecimal outboundAmount,
                         BigDecimal rate) {
        this.inboundTickerSymbol = inboundTickerSymbol;
        this.inboundAmount = inboundAmount;
        this.outboundTickerSymbol = outboundTickerSymbol;
        this.outboundAmount = outboundAmount.negate();
        this.rate = rate;
    }

}
