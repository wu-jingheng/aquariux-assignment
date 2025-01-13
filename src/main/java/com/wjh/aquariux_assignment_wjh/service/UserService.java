package com.wjh.aquariux_assignment_wjh.service;

import com.wjh.aquariux_assignment_wjh.dto.BalanceRetrievalResponse;
import com.wjh.aquariux_assignment_wjh.dto.HistoryRetrievalResponse;
import com.wjh.aquariux_assignment_wjh.exception.UserNotFoundException;
import com.wjh.aquariux_assignment_wjh.model.User;
import com.wjh.aquariux_assignment_wjh.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BalanceRetrievalResponse getWalletBalances(Long userId) {
        Optional<User> userQuery = userRepository.findById(userId);
        User user = userQuery.orElseThrow(() ->
                new UserNotFoundException(String.format("User of ID (%d) is not found", userId)));
        return new BalanceRetrievalResponse(
                "Wallet balances retrieved successfully", user.getWallet().getBalances());
    }

    public HistoryRetrievalResponse getTransactionHistory(Long userId) {
        Optional<User> userQuery = userRepository.findById(userId);
        User user = userQuery.orElseThrow(() ->
                new UserNotFoundException(String.format("User of ID (%d) is not found", userId)));
        return new HistoryRetrievalResponse(
                "Wallet balances retrieved successfully", user.getTransactionHistory());
    }
}
