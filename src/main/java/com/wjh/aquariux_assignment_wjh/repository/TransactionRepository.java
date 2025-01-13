package com.wjh.aquariux_assignment_wjh.repository;

import com.wjh.aquariux_assignment_wjh.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String>, PagingAndSortingRepository<Transaction, String> {
}
