package com.ezd.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.Dto.Status;
import com.ezd.models.Transaction;

public interface TransactionRepository  extends JpaRepository< Transaction, Long>{
    List<Transaction> findByStatus(Status status);
}
