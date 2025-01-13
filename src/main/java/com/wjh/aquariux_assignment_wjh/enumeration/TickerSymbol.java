package com.wjh.aquariux_assignment_wjh.enumeration;

import lombok.Getter;

@Getter
public enum TickerSymbol {
    TETHER("USDT"),
    ETHEREUM("ETHUSDT"),
    BITCOIN("BTCUSDT");
    private final String value;
    TickerSymbol(String value) {
        this.value = value;
    }
}
