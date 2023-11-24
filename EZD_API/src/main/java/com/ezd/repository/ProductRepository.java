package com.ezd.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ezd.models.Product;



public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p WHERE p.user_product.id = :userProductId")
    List<Product> findByUserProductId(@Param("userProductId") Long userProductId);

    @Query("SELECT p FROM Product p WHERE p.game_product.id = :gameProductId")
    List<Product> findByGameProductId(@Param("gameProductId") Long gameProductId);


}

