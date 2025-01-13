package com.wjh.aquariux_assignment_wjh.service;

import com.wjh.aquariux_assignment_wjh.dto.TradeOrderRequest;
import com.wjh.aquariux_assignment_wjh.dto.TradeOrderResponse;
import com.wjh.aquariux_assignment_wjh.enumeration.TickerSymbol;
import com.wjh.aquariux_assignment_wjh.enumeration.TransactionType;
import com.wjh.aquariux_assignment_wjh.exception.InsufficientBalanceException;
import com.wjh.aquariux_assignment_wjh.exception.UserNotFoundException;
import com.wjh.aquariux_assignment_wjh.model.Transaction;
import com.wjh.aquariux_assignment_wjh.model.User;
import com.wjh.aquariux_assignment_wjh.model.UserWallet;
import com.wjh.aquariux_assignment_wjh.repository.TransactionRepository;
import com.wjh.aquariux_assignment_wjh.repository.UserRepository;
import com.wjh.aquariux_assignment_wjh.repository.UserWalletRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Log4j2
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    private final UserWalletRepository userWalletRepository;

    private final UserRepository userRepository;

    private final PriceAggregateService priceAggregateService;

    public TransactionService(TransactionRepository transactionRepository,
                              UserWalletRepository userWalletRepository,
                              UserRepository userRepository,
                              PriceAggregateService priceAggregateService) {
        this.transactionRepository = transactionRepository;
        this.userWalletRepository = userWalletRepository;
        this.userRepository = userRepository;
        this.priceAggregateService = priceAggregateService;
    }

    @Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED)
    public TradeOrderResponse processTransaction(TradeOrderRequest request) {

        Long userId = request.getUserId();
        String orderType = request.getOrderType();
        String orderTickerSymbol = request.getOrderTickerSymbol();
        BigDecimal orderAmount = request.getOrderAmount();

        Optional<User> userQuery = userRepository.findById(userId);
        User user = userQuery.orElseThrow(() ->
                new UserNotFoundException(String.format("User of ID (%d) is not found", userId)));

        String message = validateOrderEligibility(user, orderType, orderTickerSymbol, orderAmount);

        Transaction transaction = new Transaction(user.getUserId(), orderType);
        UserWallet userWallet = user.getWallet();

        if (orderType.equalsIgnoreCase(TransactionType.SELL.getValue())) {
            performSellOrder(orderTickerSymbol, orderAmount, transaction, userWallet);
        }
        else if (orderType.equalsIgnoreCase(TransactionType.BUY.getValue())) {
            performBuyOrder(orderTickerSymbol, orderAmount, transaction, userWallet);
        }

        transactionRepository.saveAndFlush(transaction);
        userWalletRepository.saveAndFlush(userWallet);

        return new TradeOrderResponse(
                String.format("Successfully executed trade order.%s", message), new TradeOrderResponse.TransactionDTO(transaction)
        );
    }

    private String validateOrderEligibility(User user,
                                            String orderType,
                                            String orderTickerSymbol,
                                            BigDecimal orderAmount) {
        UserWallet userWallet = user.getWallet();
        String message = "";
        if (orderType.equalsIgnoreCase(TransactionType.SELL.getValue())) {
            message = validateSellOrder(orderTickerSymbol, orderAmount, userWallet);
        }
        else if (orderType.equalsIgnoreCase(TransactionType.BUY.getValue())) {
            message = validateBuyOrder(orderTickerSymbol, orderAmount, userWallet);
        }
        return message;
    }

    private String validateSellOrder(String orderTickerSymbol,
                                   BigDecimal orderAmount,
                                   UserWallet userWallet) {
        String message = "";
        BigDecimal walletBalance = userWallet.getBalance(orderTickerSymbol);
        int comparison = walletBalance.compareTo(orderAmount);
        if (comparison < 0) {
            throw new InsufficientBalanceException(String.format("""
                    
                    %s order rejected.
                    Insufficient balance of (%s) to sell at quantity of (%s).
                    """,
                    TransactionType.SELL.getValue(),
                    orderTickerSymbol,
                    orderAmount.toPlainString()));
        }

        BigDecimal latestBidQuantity = priceAggregateService.getLatestBidQuantity(orderTickerSymbol);
        comparison = orderAmount.compareTo(latestBidQuantity);
        if (comparison > 0) {
            // Notify user that only a portion of the requested amount will be sold at given price due insufficient demand.
            message = " Only a portion of the requested sale amount was executed due to insufficient demand at the current price.";
        }
        return message;
    }

    private String validateBuyOrder(String orderTickerSymbol,
                                    BigDecimal orderAmount,
                                    UserWallet userWallet) {
        String message = "";
        BigDecimal walletBalance = userWallet.getBalance("USDT");
        BigDecimal latestAskPrice = priceAggregateService.getLatestAskPrice(orderTickerSymbol);
        BigDecimal totalPriceByRequestedAmount = latestAskPrice.multiply(orderAmount);
        int comparison = totalPriceByRequestedAmount.compareTo(walletBalance);

        if (comparison > 0) {
            throw new InsufficientBalanceException(String.format("""
                    
                    %s order rejected.
                    Insufficient balance of (%s) to buy (%s) at quantity of (%s).
                    """,
                    TransactionType.BUY.getValue(),
                    TickerSymbol.TETHER.getValue(),
                    orderTickerSymbol,
                    orderAmount.toPlainString()));
        }

        BigDecimal latestAskQuantity = priceAggregateService.getLatestAskQuantity(orderTickerSymbol);
        comparison = latestAskQuantity.compareTo(orderAmount);

        if (comparison > 0) {
            // Notify user that only a portion of the requested amount will be purchased at given price due insufficient supply.
            message = " Only a portion of the requested purchase amount was executed due to insufficient supply at the current price.";
        }
        return message;
    }

    private void performSellOrder(String orderTickerSymbol, BigDecimal orderAmount, Transaction transaction, UserWallet userWallet) {
        BigDecimal latestBidPrice = priceAggregateService.getLatestBidPrice(orderTickerSymbol);
        BigDecimal inboundAmount = latestBidPrice.multiply(orderAmount);
        transaction.populate(
                TickerSymbol.TETHER.getValue(),
                inboundAmount,
                orderTickerSymbol,
                orderAmount,
                latestBidPrice
        );
        userWallet.addCurrency(TickerSymbol.TETHER.getValue(), inboundAmount);
        userWallet.deductCurrency(orderTickerSymbol, orderAmount);
    }

    private void performBuyOrder(String orderTickerSymbol, BigDecimal orderAmount, Transaction transaction, UserWallet userWallet) {
        BigDecimal latestAskPrice = priceAggregateService.getLatestAskPrice(orderTickerSymbol);
        BigDecimal outboundAmount = latestAskPrice.multiply(orderAmount);
        transaction.populate(
                orderTickerSymbol,
                orderAmount,
                TickerSymbol.TETHER.getValue(),
                outboundAmount,
                latestAskPrice
        );
        userWallet.addCurrency(orderTickerSymbol, orderAmount);
        userWallet.deductCurrency(TickerSymbol.TETHER.getValue(), outboundAmount);
    }
}
