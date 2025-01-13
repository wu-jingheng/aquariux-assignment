package com.wjh.aquariux_assignment_wjh.service;

import com.wjh.aquariux_assignment_wjh.dto.PriceRetrievalResponse;
import com.wjh.aquariux_assignment_wjh.enumeration.Binance;
import com.wjh.aquariux_assignment_wjh.enumeration.Exchange;
import com.wjh.aquariux_assignment_wjh.enumeration.Huobi;
import com.wjh.aquariux_assignment_wjh.model.AggregatedPrice;
import com.wjh.aquariux_assignment_wjh.model.ExchangeTicker;
import com.wjh.aquariux_assignment_wjh.repository.AggregatedPriceRepository;
import com.wjh.aquariux_assignment_wjh.utility.TickerMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

@Service
public class PriceAggregateService {

    private final AggregatedPriceRepository aggregatedPriceRepository;

    private final TickerMapper tickerMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    private Map<String, AggregatedPrice> latestAggregatedPrices;

    public PriceAggregateService(AggregatedPriceRepository aggregatedPriceRepository,
                                 TickerMapper tickerMapper) {
        this.aggregatedPriceRepository = aggregatedPriceRepository;
        this.tickerMapper = tickerMapper;
    }

    @Scheduled(fixedRate = 10000)
    public void fetchAndStorePrices() {
        Map<String, AggregatedPrice> aggregatedPrices = new HashMap<>();

        Map<String, ExchangeTicker> binanceData = fetchBinanceData();
        Map<String, ExchangeTicker> huobiData = fetchHuobiData();
        LocalDateTime aggregateTimestamp = LocalDateTime.now(ZoneId.of("UTC"));

        processData(Exchange.BINANCE.getValue(), binanceData, aggregatedPrices, aggregateTimestamp);
        processData(Exchange.HUOBI.getValue(), huobiData, aggregatedPrices, aggregateTimestamp);

        aggregatedPriceRepository.saveAll(aggregatedPrices.values());
        latestAggregatedPrices = aggregatedPrices;
    }

    private Map<String, ExchangeTicker> fetchBinanceData() {
        ResponseEntity<String> binanceResponse = restTemplate.getForEntity(Binance.URL.getValue(), String.class);
        return tickerMapper.extractBinanceTickers(binanceResponse.getBody());
    }

    private Map<String, ExchangeTicker> fetchHuobiData() {
        ResponseEntity<String> binanceResponse = restTemplate.getForEntity(Huobi.URL.getValue(), String.class);
        return tickerMapper.extractHuobiTickers(binanceResponse.getBody());
    }

    private void processData(String exchange,
                             Map<String, ExchangeTicker> exchangeData,
                             Map<String, AggregatedPrice> aggregatedPrices,
                             LocalDateTime aggregateTimestamp) {
        for (ExchangeTicker ticker : exchangeData.values()) {
            AggregatedPrice aggregatedPrice = aggregatedPrices.getOrDefault(
                    ticker.getSymbol(), new AggregatedPrice(ticker.getSymbol(), aggregateTimestamp)
            );

            int bidComparison = ticker.getBidPrice().compareTo(aggregatedPrice.getBidPrice());
            if (bidComparison >= 0) {
                aggregatedPrice.setBidPrice(ticker.getBidPrice());
                aggregatedPrice.setBidQuantity(ticker.getBidQuantity());
                aggregatedPrice.setBidExchangePlatform(exchange);
            }

            int askComparison = ticker.getAskPrice().compareTo(aggregatedPrice.getAskPrice());
            if (askComparison <= 0) {
                aggregatedPrice.setAskPrice(ticker.getAskPrice());
                aggregatedPrice.setAskQuantity(ticker.getAskQuantity());
                aggregatedPrice.setAskExchangePlatform(exchange);
            }
            aggregatedPrices.put(ticker.getSymbol(), aggregatedPrice);
        }
    }

    public PriceRetrievalResponse getLatestAggregatedPrices() {
        return new PriceRetrievalResponse(
                "Latest aggregated prices retrieved successfully", latestAggregatedPrices);
    }

    public BigDecimal getLatestBidPrice(String tickerSymbol) {
        return latestAggregatedPrices.get(tickerSymbol).getBidPrice();
    }

    public BigDecimal getLatestBidQuantity(String tickerSymbol) {
        return latestAggregatedPrices.get(tickerSymbol).getBidQuantity();
    }

    public BigDecimal getLatestAskPrice(String tickerSymbol) {
        return latestAggregatedPrices.get(tickerSymbol).getAskPrice();
    }

    public BigDecimal getLatestAskQuantity(String tickerSymbol) {
        return latestAggregatedPrices.get(tickerSymbol).getAskQuantity();
    }
}
