package com.ezd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezd.models.Purchase;

public interface PurchaseRepository extends JpaRepository<Purchase, Long> {
	
//    @Query("SELECT p FROM Purchase p WHERE p.userId.id = :userId")
//    List<Purchase> findByAuth_purchase(@Param("userId") Long userId);
	@Query("SELECT p FROM Purchase p WHERE p.auth_purchase.id = :userPurchaseId")
	List<Purchase> findByUserPurchasesId(@Param("userPurchaseId") Long userPurchaseId);
}
