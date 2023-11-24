package com.ezd.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ezd.models.Purchase;
import com.ezd.repository.PurchaseRepository;
import com.ezd.service.PurchaseService;

@RestController
@RequestMapping("/api/purchase")
public class PurchaseController {

	@Autowired
	private PurchaseService purchaseService;
	@Autowired
	private PurchaseRepository purchaseRepository;
	@GetMapping("/")
	public ResponseEntity<List<Purchase>> getAllPurchase() {
		List<Purchase> purchase = purchaseService.getAllPurchase();
		return new ResponseEntity<>(purchase, HttpStatus.OK);
	}

	@GetMapping("/byUser/{userId}")
	public ResponseEntity<List<Purchase>> getAllPurchaseById(@PathVariable Long userId) {
		List<Purchase> purchase = purchaseRepository.findByUserPurchasesId(userId);
		return new ResponseEntity<>(purchase, HttpStatus.OK);
	}

	@PostMapping("/buyItem")
	public ResponseEntity<?> buyItem(@RequestParam("userId") Long userId, @RequestParam("itemId") Long itemId,
			@RequestParam("quantity") int quantity) {
		try {
			purchaseService.buyItem(userId, itemId, quantity);
			return ResponseEntity.ok("Item purchased successfully.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}
}
