package com.ezd.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezd.models.Auth;
import com.ezd.models.LuckySpin;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.LuckySpinRepository;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional

public class LuckySpinService 
{
	@Autowired
	LuckySpinRepository luckySpinRepository;
	@Autowired
    private AuthRepository authRepository;

	@Transactional

	public void createLuckySpinTransaction(Auth user, BigDecimal point) {
        // Tạo giao dịch Lucky Spin
        LuckySpin luckySpin = new LuckySpin();
        luckySpin.setUser_lucky(user);
        luckySpin.setPoint(point);
        luckySpin.setLuckyTime(LocalDateTime.now());
        
        luckySpinRepository.save(luckySpin);
        
        // Cập nhật số tiền cho Auth
        BigDecimal currentBalance = user.getBalance();
        BigDecimal poin = luckySpin.getPoint();
        BigDecimal newBalance = currentBalance.add(poin);
        user.setBalance(newBalance);
        authRepository.save(user);
    }
	
	 @Transactional(readOnly = true)
	    public List<LuckySpin> findLuckySpinsByUserId(Long userId) {
	        return luckySpinRepository.findLuckySpinsByUserId(userId);
	    }
	
}
