package com.wjh.aquariux_assignment_wjh.enumeration;

import lombok.Getter;

@Getter
public enum Exchange {
    BINANCE("Binance"),
    HUOBI("Huobi");

    private final String value;
    Exchange(String value) {
        this.value = value;
    }

}
