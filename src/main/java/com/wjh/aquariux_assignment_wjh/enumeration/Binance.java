package com.wjh.aquariux_assignment_wjh.enumeration;

import lombok.Getter;

@Getter
public enum Binance {
    SYMBOL("symbol"),
    BID_PRICE("bidPrice"),
    BID_QUANTITY("bidQty"),
    ASK_PRICE("askPrice"),
    ASK_QUANTITY("askQty"),
    URL("https://api.binance.com/api/v3/ticker/bookTicker");

    private final String value;
    Binance(String value) {
        this.value = value;
    }

}
