package com.ezd.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Product;
import com.ezd.models.Review;
public interface ReviewRepository extends JpaRepository<Review, Long> {
	List<Review> findByProductAndName(Product product, String name);
}
