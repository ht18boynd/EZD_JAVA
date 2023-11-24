package com.ezd.controllers;

import com.ezd.models.Donate;
import com.ezd.service.DonateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/donates")
public class DonateController {

	private final DonateService donateService;

	@Autowired
	public DonateController(DonateService donateService) {
		this.donateService = donateService;
	}

	@PostMapping("/donate")
	public void donate(@RequestParam("fromUserId") Long fromUserId, @RequestParam("toUserId") Long toUserId,
			@RequestParam("amount") BigDecimal amount) throws Exception {
		donateService.donate(fromUserId, toUserId, amount);
	}

	@PostMapping("/donateByItem")
	public ResponseEntity<?> donate(@RequestParam("fromUserId") Long fromUserId,
			@RequestParam("toUserId") Long toUserId, @RequestParam("itemId") Long itemId,
			@RequestParam("quantity") int quantity) {
		try {
			donateService.donateByItem(fromUserId, toUserId, quantity,itemId);
			return ResponseEntity.ok("Donation successful.");
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
	}

	@GetMapping("/all")
	public List<Donate> getAllDonates() {
		return donateService.getAllDonates();
	}

	@GetMapping("/user/{userId}")
	public List<Donate> getDonatesByUserId(@PathVariable Long userId) {
		return donateService.findDonatesByUserId(userId);
	}
}
