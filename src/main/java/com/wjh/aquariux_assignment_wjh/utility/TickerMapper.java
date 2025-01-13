package com.wjh.aquariux_assignment_wjh.utility;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.wjh.aquariux_assignment_wjh.enumeration.Binance;
import com.wjh.aquariux_assignment_wjh.enumeration.Huobi;
import com.wjh.aquariux_assignment_wjh.enumeration.TickerSymbol;
import com.wjh.aquariux_assignment_wjh.exception.TickerMappingException;
import com.wjh.aquariux_assignment_wjh.model.ExchangeTicker;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Component
public class TickerMapper {
    public Map<String, ExchangeTicker> extractBinanceTickers(String jsonResponse) {
        Map<String, ExchangeTicker> result = new HashMap<>();
        JsonNode rootNode;
        try {
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(jsonResponse);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    String tickerSymbol = node.get(Binance.SYMBOL.getValue()).asText();
                    if (tickerSymbol.equalsIgnoreCase(TickerSymbol.ETHEREUM.getValue()) ||
                            tickerSymbol.equalsIgnoreCase(TickerSymbol.BITCOIN.getValue())) {
                        ExchangeTicker ticker = new ExchangeTicker();
                        ticker.setSymbol(tickerSymbol);
                        ticker.setBidPrice(new BigDecimal(node.get(Binance.BID_PRICE.getValue()).asText()));
                        ticker.setBidQuantity(new BigDecimal(node.get(Binance.BID_QUANTITY.getValue()).asText()));
                        ticker.setAskPrice(new BigDecimal(node.get(Binance.ASK_PRICE.getValue()).asText()));
                        ticker.setAskQuantity(new BigDecimal(node.get(Binance.ASK_QUANTITY.getValue()).asText()));
                        result.put(tickerSymbol, ticker);
                    }
                }
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new TickerMappingException(e.getMessage());
        }
    }

    public Map<String, ExchangeTicker> extractHuobiTickers(String jsonResponse) {
        Map<String, ExchangeTicker> result = new HashMap<>();
        JsonNode rootNode;
        try {
            ObjectMapper mapper = new ObjectMapper();
            rootNode = mapper.readTree(jsonResponse);
            if (rootNode.isArray()) {
                for (JsonNode node : rootNode) {
                    String tickerSymbol = node.get(Huobi.SYMBOL.getValue()).asText();
                    if (tickerSymbol.equalsIgnoreCase(TickerSymbol.ETHEREUM.getValue()) ||
                            tickerSymbol.equalsIgnoreCase(TickerSymbol.BITCOIN.getValue())) {
                        ExchangeTicker ticker = new ExchangeTicker();
                        ticker.setSymbol(tickerSymbol);
                        ticker.setBidPrice(new BigDecimal(node.get(Huobi.BID_PRICE.getValue()).asText()));
                        ticker.setBidQuantity(new BigDecimal(node.get(Huobi.BID_QUANTITY.getValue()).asText()));
                        ticker.setAskPrice(new BigDecimal(node.get(Huobi.ASK_PRICE.getValue()).asText()));
                        ticker.setAskQuantity(new BigDecimal(node.get(Huobi.ASK_QUANTITY.getValue()).asText()));
                        result.put(tickerSymbol, ticker);
                    }
                }
            }
            return result;
        } catch (JsonProcessingException e) {
            throw new TickerMappingException(e.getMessage());
        }
    }
}
