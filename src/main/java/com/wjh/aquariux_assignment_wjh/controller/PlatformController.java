package com.wjh.aquariux_assignment_wjh.controller;

import com.wjh.aquariux_assignment_wjh.dto.*;
import com.wjh.aquariux_assignment_wjh.service.PriceAggregateService;
import com.wjh.aquariux_assignment_wjh.service.TransactionService;
import com.wjh.aquariux_assignment_wjh.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequestMapping("/api")
public class PlatformController {

    private final PriceAggregateService priceAggregateService;

    private final UserService userService;

    private final TransactionService transactionService;

    public PlatformController(PriceAggregateService priceAggregateService,
                              UserService userService,
                              TransactionService transactionService) {
        this.priceAggregateService = priceAggregateService;
        this.userService = userService;
        this.transactionService = transactionService;
    }

    @GetMapping("/price")
    public ResponseEntity<PriceRetrievalResponse> getLatestPrices() {
        PriceRetrievalResponse response = priceAggregateService.getLatestAggregatedPrices();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/wallet")
    public ResponseEntity<BalanceRetrievalResponse> getWalletBalances(@RequestParam Long userId) {
        BalanceRetrievalResponse response = userService.getWalletBalances(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/history")
    public ResponseEntity<HistoryRetrievalResponse> getTransactionHistory(@RequestParam Long userId) {
        HistoryRetrievalResponse response = userService.getTransactionHistory(userId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trade")
    public ResponseEntity<TradeOrderResponse> executeTrade(@RequestBody TradeOrderRequest request) {
        TradeOrderResponse response = transactionService.processTransaction(request);
        return ResponseEntity.ok(response);
    }
}
