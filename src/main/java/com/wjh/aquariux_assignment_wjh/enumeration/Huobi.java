package com.wjh.aquariux_assignment_wjh.enumeration;

import lombok.Getter;

@Getter
public enum Huobi {
    SYMBOL("symbol"),
    BID_PRICE("bid"),
    BID_QUANTITY("bidSize"),
    ASK_PRICE("ask"),
    ASK_QUANTITY("askSize"),
    URL("https://api.huobi.pro/market/tickers");

    private final String value;
    Huobi(String value) {
        this.value = value;
    }

}
