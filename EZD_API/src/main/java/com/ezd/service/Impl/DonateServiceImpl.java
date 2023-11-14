package com.ezd.service.Impl;

import com.ezd.models.Auth;
import com.ezd.models.Donate;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.DonateRepository;
import com.ezd.service.DonateService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class DonateServiceImpl implements DonateService {

    private final DonateRepository donateRepository;

    @Autowired
    private AuthRepository authRepository;
    
    @Autowired
    public DonateServiceImpl(DonateRepository donateRepository) {
        this.donateRepository = donateRepository;
    }

    @Override
    @Transactional
    public void donate (Long fromUserId, Long toUserId, BigDecimal amount) throws Exception {
    	// Kiểm tra số dư của người gửi
    	Auth fromUser = authRepository.findById(fromUserId).orElse(null);
    	Auth toUser = authRepository.findById(toUserId).orElse(null);

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
    @Transactional(readOnly = true)
    public List<Donate> getAllDonates() {
        return donateRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Donate> findDonatesByUserId(Long userId) {
        return donateRepository.findByUserFromIdOrUserToId(userId);
    }
    }
