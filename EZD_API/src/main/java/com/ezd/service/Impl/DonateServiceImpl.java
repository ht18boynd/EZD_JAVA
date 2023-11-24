package com.ezd.service.Impl;

import com.ezd.models.Auth;
import com.ezd.models.Donate;
import com.ezd.models.Item;
import com.ezd.models.Purchase;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.DonateRepository;
import com.ezd.repository.ItemRespository;
import com.ezd.repository.PurchaseRepository;
import com.ezd.service.DonateService;
import com.ezd.service.PurchaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonateServiceImpl implements DonateService {
	@Autowired
	private DonateRepository donateRepository;

	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private ItemRespository itemRespository;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@Override
	@Transactional
	public void donate(Long fromUserId, Long toUserId, BigDecimal amount) throws Exception {
		// Kiểm tra số dư của người gửi
		Auth fromUser = getUserById(fromUserId);
		Auth toUser = getUserById(toUserId);

		if (fromUser.getBalance().compareTo(amount) >= 0) {
			// Tạo đối tượng Donate
			Donate donate = new Donate();
			donate.setAmount(amount);
			donate.setUser_from(fromUser);
			donate.setUser_to(toUser);
			donate.setTransactionDate(LocalDateTime.now());

			// Lưu thông tin donate vào database
			donateRepository.save(donate);

			// Cập nhật số dư người gửi và người nhận
			fromUser.setBalance(fromUser.getBalance().subtract(amount));
			toUser.setBalance(toUser.getBalance().add(amount));
		} else {
			throw new Exception("Not Enought Balance");
		}
	}

	@Override
	@Transactional
	public void donateByItem(Long fromUserId, Long toUserId, int quantity, Long itemId) throws Exception {
		Auth fromUser = getUserById(fromUserId);
		Auth toUser = getUserById(toUserId);

		Purchase purchase = getPurchaseByUserAndItemId(fromUser, itemId);
		// Thực hiện logic donate ở đây
		// Kiểm tra xem người donate có đủ số lượng donate không
		if (purchase != null && purchase.getQuantity() >= 0) {

			Donate donate = new Donate();
			donate.setUser_from(fromUser);
			donate.setUser_to(toUser);
			donate.setItems(purchase.getItem_purchase());
			donate.setQuantity(quantity);
			donate.setTransactionDate(LocalDateTime.now());

			// Giảm số lượng của purchase cụ thể cho item đó
			purchase.setQuantity(purchase.getQuantity() - quantity);
			
			// Tăng số lượng của purchase cụ thể cho item đó ở người nhận donate
			Purchase recipientPurchase = getPurchaseByUserAndItemId(toUser, itemId);
			if (recipientPurchase != null) {
				recipientPurchase.setQuantity(recipientPurchase.getQuantity() + quantity);
				purchaseRepository.save(recipientPurchase);
			} else {
				recipientPurchase = new Purchase();
				recipientPurchase.setAuth_purchase(toUser);
				recipientPurchase.setItem_purchase(purchase.getItem_purchase());
				recipientPurchase.setQuantity(quantity);
				toUser.getPurchases().add(recipientPurchase);
				purchaseRepository.save(recipientPurchase);
			}

			fromUser.decreaseItemQuantity(purchase, quantity);
			toUser.increaseItemQuantity(purchase, quantity);

			donateRepository.save(donate);
			authRepository.save(fromUser);
			authRepository.save(toUser);
		} else {
			throw new RuntimeException("Not enough quantity or balance to make the donation.");
		}

	}

	@Override
	@Transactional(readOnly = true)
	public List<Donate> getAllDonates() {
		return donateRepository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public List<Donate> findDonatesByUserId(Long userId) {
		return donateRepository.findByUserFromIdOrUserToId(userId);
	}

	private Auth getUserById(Long userId) {
		return authRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found."));
	}

//	private Item getItemById(Long itemId) {
//		return itemRespository.findById(itemId).orElseThrow(() -> new RuntimeException("Item not found."));
//	}

	private Purchase getPurchaseByUserAndItemId(Auth user, Long itemId) {
		return user.getPurchases().stream().filter(purchase -> purchase.getItem_purchase().getId().equals(itemId))
				.findFirst().orElse(null);
	}
}
