package com.wjh.aquariux_assignment_wjh.dto;

import com.wjh.aquariux_assignment_wjh.model.Transaction;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class TradeOrderResponse {
    private String message;
    private TransactionDTO transaction;
    @Getter
    @Setter
    @NoArgsConstructor
    public static class TransactionDTO {
        public TransactionDTO(Transaction transaction) {
            this.transactionId = transaction.getTransactionId();
            this.transactionType = transaction.getTransactionType();
            this.purchasedCurrency = transaction.getInboundTickerSymbol();
            this.purchasedAmount = transaction.getInboundAmount();
            this.baseCurrency = transaction.getOutboundTickerSymbol();
            this.baseCost = transaction.getOutboundAmount();
            this.transactionRate = transaction.getRate();
            this.orderTimestamp = transaction.getOrderTimestamp();
        }
        private String transactionId;
        private String transactionType;
        private String purchasedCurrency;
        private BigDecimal purchasedAmount;
        private String baseCurrency;
        private BigDecimal baseCost;
        private BigDecimal transactionRate;
        private LocalDateTime orderTimestamp;
    }
}
