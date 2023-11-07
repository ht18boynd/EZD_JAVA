package com.ezd.service;

import com.ezd.Dto.Status;
import com.ezd.models.Auth;
import com.ezd.models.Transaction;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AuthRepository authRepository;

    @Transactional
    public   void saveTransaction(Transaction transaction) {
        Auth user = transaction.getUser_transaction(); // Lấy thông tin người dùng từ transaction

        if (user != null) {
            // Liên kết thông tin giao dịch với người dùng
            transaction.setUser_transaction(user);
            transactionRepository.save(transaction);
        }
     
    }

    @Transactional
    public void adminCheckTransaction(Long transactionId, Status newStatus) {
        Transaction transaction = transactionRepository.findById(transactionId).orElse(null);

        if (transaction != null && !transaction.isCheckedByAdmin()) {
            // Kiểm tra trạng thái mới
            if (newStatus == Status.SUCCESS || newStatus == Status.FAILED) {
                // Cập nhật trạng thái giao dịch
                transaction.setStatus(newStatus);
                transaction.setCheckedByAdmin(true);
                transaction.setAdminCheckTime(LocalDateTime.now());
                transactionRepository.save(transaction);

                // Nếu là "SUCCESS", cộng số tiền vào tài khoản của người dùng
                if (newStatus == Status.SUCCESS) {
                    Auth user = transaction.getUser_transaction();
                    BigDecimal currentBalance = user.getBalance();
                    BigDecimal transactionAmount = transaction.getAmount();
                    BigDecimal newBalance = currentBalance.add(transactionAmount);
                    user.setBalance(newBalance);
                    // Lưu thông tin người dùng sau khi cập nhật số tiền
                    authRepository.save(user);
                }
            }
        }
    
}

    public List<Transaction> getTransactionsByStatus(Status status) {
        return transactionRepository.findByStatus(status);
    }

}