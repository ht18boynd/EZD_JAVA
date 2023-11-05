package com.ezd.controllers;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Auth;
import com.ezd.models.LuckySpin;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.LuckySpinRepository;
import com.ezd.service.LuckySpinService;

@RestController
@RequestMapping("/api/lucky-spin")
public class LuckySpinController {
    @Autowired
    private LuckySpinRepository luckySpinRepository;
    
    @Autowired
    
    private LuckySpinService luckySpinService;
    
    @Autowired
    
    private AuthRepository authRepository;

    @PostMapping("/create")
    public ResponseEntity<String> createLuckySpinTransaction(@RequestParam("userId") Long userId, @RequestParam("point") BigDecimal point) {
        // Tìm Auth dựa trên userId
        Auth user = authRepository.findById(userId).orElse(null);

        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        // Tạo giao dịch Lucky Spin và cập nhật số tiền cho Auth
        luckySpinService.createLuckySpinTransaction(user, point);

        return new ResponseEntity<>("Lucky Spin transaction created successfully", HttpStatus.OK);
    }

    @GetMapping("/")
    public ResponseEntity<List<LuckySpin>> getAllLuckSpins() {
        List<LuckySpin> luckySpins = luckySpinRepository.findAll();

        return new ResponseEntity<>(luckySpins, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<LuckySpin> getLuckySpinById(@PathVariable Long id) {
        Optional<LuckySpin> luckySpin = luckySpinRepository.findById(id);

        if (luckySpin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(luckySpin.get(), HttpStatus.OK);
    }
    @GetMapping("/auth/{userId}")
    public ResponseEntity<Auth> getAuthById(@PathVariable Long userId) {
        Optional<Auth> auth = authRepository.findById(userId);

        if (auth.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(auth.get(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<LuckySpin>> getLuckySpinsByUserId(@PathVariable Long userId) {
        List<LuckySpin> luckySpins = luckySpinService.findLuckySpinsByUserId(userId);

        if (luckySpins.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(luckySpins, HttpStatus.OK);
    }

}

