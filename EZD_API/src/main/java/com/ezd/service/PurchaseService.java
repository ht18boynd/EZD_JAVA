package com.ezd.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ezd.models.Auth;
import com.ezd.models.Item;
import com.ezd.models.Purchase;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.ItemRespository;
import com.ezd.repository.PurchaseRepository;

@Service
public class PurchaseService {

	@Autowired
	private PurchaseRepository purchaseRepository;

	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private ItemRespository itemRepository;

	public List<Purchase> getAllPurchase() {
		return purchaseRepository.findAll();
	}

	public void buyItem(Long userId, Long itemId, int quantity) {
//		Auth user = getUserById(userId);
		Auth user = authRepository.findById(userId).orElse(null);
		Item item = itemRepository.findById(itemId).orElse(null);
		
		// Thực hiện logic mua ở đây
		// Ví dụ: Giả sử giá của item được nhân với số lượng để tính tổng tiền
		BigDecimal totalPrice = item.getPrice().multiply(BigDecimal.valueOf(quantity));

		// Kiểm tra xem người dùng có đủ tiền để mua không
		if (user.getBalance().compareTo(totalPrice) >= 0) {
			// Trừ tiền và thêm item vào danh sách người dùng
			user.setBalance(user.getBalance().subtract(totalPrice));

			// Tạo đối tượng Purchase và lưu vào database
			// Kiểm tra xem đã có thông tin mua cho item chưa
			Purchase purchase = getPurchaseByUserAndItem(user, item);
			if (purchase != null) {
	            // Kiểm tra giá trị quantity được truyền vào
//	            System.out.println("Quantity from request: " + quantity);
	            
	            // Kiểm tra giá trị quantity trong purchase
//	            System.out.println("Quantity in purchase before update: " + purchase.getQuantity());
				// Nếu đã có, cập nhật số lượng
				purchase.setQuantity((purchase.getQuantity() + quantity));
//	            System.out.println("Quantity in purchase after update: " + purchase.getQuantity());
	            purchase.setItem_purchase(item);
//	            System.out.println("Item in purchase after update: " + purchase.getItem_purchase());
	            purchaseRepository.save(purchase);
	            
			} else {
				// Nếu chưa có, tạo mới thông tin mua
				purchase = new Purchase();
				purchase.setAuth_purchase(user);
				purchase.setItem_purchase(item);
				purchase.setQuantity(quantity);
				user.getPurchases().add(purchase);
				
				purchaseRepository.save(purchase);
			}
			
			// Lưu thay đổi vào database
			authRepository.save(user);
		} else {
			throw new RuntimeException("Not enough balance to make the purchase.");
		}
	}

	private Purchase getPurchaseByUserAndItem(Auth user, Item item) {
		return user.getPurchases().stream().filter(purchase -> purchase.getItem_purchase().equals(item)).findFirst()
				.orElse(null);
	}
}
