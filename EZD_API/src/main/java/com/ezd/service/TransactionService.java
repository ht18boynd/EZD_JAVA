package com.ezd.service;

import com.ezd.models.Auth;
import com.ezd.models.Transaction;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.TransactionRepository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {
	 @Autowired
	    private TransactionRepository transactionRepository;

	 @Autowired 
	 private AuthRepository authRepository;
	 public Transaction saveTransaction(Transaction transaction) {
		    Auth user = transaction.getUser(); // Lấy thông tin người dùng trực tiếp từ transaction

		    if (user != null) {
		        // Liên kết thông tin giao dịch với người dùng
		        transaction.setUser(user);
		        transactionRepository.save(transaction);
		    }
		    return transaction;
		}

}
