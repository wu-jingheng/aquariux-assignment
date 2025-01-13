package com.wjh.aquariux_assignment_wjh.enumeration;

import lombok.Getter;

@Getter
public enum TransactionType {
    BUY("BUY"),
    SELL("SELL");

    private final String value;
    TransactionType(String value) {
        this.value = value;
    }

}
