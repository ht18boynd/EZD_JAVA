package com.ezd.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Transaction;

public interface TransactionRepository  extends JpaRepository< Transaction, Long>{

}
