package com.ezd.service;

import com.ezd.models.Auth;
import com.ezd.models.Donate;

import java.math.BigDecimal;
import java.util.List;

public interface DonateService {
	
    void donate(Long fromUserId, Long toUserId, BigDecimal amount) throws Exception;
    void donateByItem(Long fromUserId, Long toUserId, int quantity, Long itemId) throws Exception;
    List<Donate> getAllDonates();

    List<Donate> findDonatesByUserId(Long userId);
}