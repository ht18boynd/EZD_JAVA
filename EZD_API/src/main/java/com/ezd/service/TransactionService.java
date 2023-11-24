package com.ezd.service;

import com.ezd.Dto.Status;
import com.ezd.models.Auth;
import com.ezd.models.Rank;
import com.ezd.models.Transaction;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.RankRepository;
import com.ezd.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionService {
	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private RankRepository rankRepository;

	@Transactional
	public void saveTransaction(Transaction transaction) {
		Auth user = transaction.getUser_transaction(); // Lấy thông tin người dùng từ transaction

		if (user != null) {
			// Liên kết thông tin giao dịch với người dùng
			transaction.setUser_transaction(user);
			transactionRepository.save(transaction);
			// Kiểm tra và cập nhật Rank
//			updateRankForUser(user, transaction.getAmount());
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

					// Kiểm tra và cập nhật Rank của người chơi
					updateRankForUser(user, transactionAmount);
				}
			}
		}
	}

	private void updateRankForUser(Auth user, BigDecimal transactionAmount) {
		// Lấy danh sách Rank từ cơ sở dữ liệu
		List<Rank> ranks = rankRepository.findAll();

		// Lấy số dư hiện tại của người chơi
		BigDecimal userBalance = user.getBalance();

		for (Rank rank : ranks) {
			if (userBalance.compareTo(rank.getMinimum_balance()) >= 0
					&& userBalance.compareTo(rank.getMaximum_balance()) < 0) {
				// Nếu số dư nằm trong khoảng của Rank, cập nhật Rank mới
				user.setCurrentRank(rank);

				// Lưu thay đổi vào cơ sở dữ liệu
				authRepository.save(user);
				return;
			}
		}

		// Nếu không thuộc vào bất kỳ Rank nào, giữ nguyên Rank 0
		user.setCurrentRank(null); // hoặc có thể là user.setCurrentRank(rank0) nếu có một Rank đặc biệt cho Rank 0
		authRepository.save(user);
	}

	public List<Transaction> getTransactionsByStatus(Status status) {
		return transactionRepository.findByStatus(status);
	}

}