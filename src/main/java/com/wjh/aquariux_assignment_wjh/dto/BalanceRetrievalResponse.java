package com.wjh.aquariux_assignment_wjh.dto;

import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Map;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class BalanceRetrievalResponse {
    private String message;
    private Map<String, BigDecimal> balances;
}
