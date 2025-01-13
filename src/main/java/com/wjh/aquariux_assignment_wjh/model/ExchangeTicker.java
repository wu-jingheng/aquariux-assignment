package com.wjh.aquariux_assignment_wjh.model;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class ExchangeTicker {
    private String symbol;

    private BigDecimal bidPrice;

    private BigDecimal bidQuantity;

    private BigDecimal askPrice;

    private BigDecimal askQuantity;

}
