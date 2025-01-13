package com.wjh.aquariux_assignment_wjh.dto;

import com.wjh.aquariux_assignment_wjh.model.Transaction;
import lombok.AllArgsConstructor;
import lombok.Generated;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Generated
@AllArgsConstructor
public class HistoryRetrievalResponse {
    private String message;
    private List<Transaction> transactionHistory;
}
