package com.ezd.controllers;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.Dto.Role;
import com.ezd.Dto.Status;
import com.ezd.models.Auth;
import com.ezd.models.Transaction;
import com.ezd.service.TransactionService;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

	@Autowired
	private TransactionService transactionService;

	@PostMapping("/save")
	public void saveTransaction(@RequestParam("userId") Long userId, @RequestParam("amount") String amount) {
		Transaction transaction = new Transaction();
		 BigDecimal newAmount = new BigDecimal(amount);
		// Thực hiện thiết lập các thông tin giao dịch, ví dụ:
		
		// Đặt thông tin người dùng vào giao dịch
		Auth user = new Auth();
		user.setRole(Role.USER);

		user.setId(userId);
		transaction.setUser_transaction(user);
		transaction.setAmount(newAmount);
		transaction.setStatus(Status.PENDING);
		transaction.setTransactionTime(LocalDateTime.now());
		// Set other fields of the transaction as needed

		 transactionService.saveTransaction(transaction);
	}

	@PutMapping("/admin-check")
	public void adminCheckTransaction(@RequestParam("transactionId") Long transactionId,
			@RequestParam("newStatus") Status newStatus) {
		transactionService.adminCheckTransaction(transactionId, newStatus);
	}
	
	@GetMapping("/byStatus")
    public List<Transaction> getTransactionsByStatus(@RequestParam("status") Status status) {
        return transactionService.getTransactionsByStatus(status);
    }
}
