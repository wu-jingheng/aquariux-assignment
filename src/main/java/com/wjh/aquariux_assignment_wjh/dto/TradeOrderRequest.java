package com.wjh.aquariux_assignment_wjh.dto;

import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Generated
public class TradeOrderRequest {
    private Long userId;
    private String orderType;
    private String orderTickerSymbol;
    private BigDecimal orderAmount;
}
