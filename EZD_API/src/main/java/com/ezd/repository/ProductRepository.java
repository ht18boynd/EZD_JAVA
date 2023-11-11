package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
