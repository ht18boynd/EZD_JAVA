package com.ezd.controllers;

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

import com.ezd.models.Product;
import com.ezd.models.Review;
import com.ezd.repository.ProductRepository;
import com.ezd.repository.ReviewRepository;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
	 @Autowired
	    private ReviewRepository reviewRepository;

	    @Autowired
	    private ProductRepository productService;  // Assuming you have a ProductService

	    // Endpoint to get reviews for a specific product and reviewer
	    @GetMapping("/")
	    public ResponseEntity<List<Review>> getAllReviews() {
	        try {
	            List<Review> reviews = reviewRepository.findAll();

	            if (!reviews.isEmpty()) {
	                return new ResponseEntity<>(reviews, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    
	    @GetMapping("/getReview/{reviewId}")
	    public ResponseEntity<Review> getReviewById(@PathVariable Long reviewId) {
	        try {
	            Optional<Review> reviewOptional = reviewRepository.findById(reviewId);

	            if (reviewOptional.isPresent()) {
	                Review review = reviewOptional.get();
	                return new ResponseEntity<>(review, HttpStatus.OK);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }

	    // Endpoint to add a new review
	    @PostMapping("/addReview")
	    public ResponseEntity<Review> addReview(
	            @RequestParam Long productId,
	            @RequestParam String reviewerName,
	            @RequestParam Integer rating,
	            @RequestParam String comment) {
	        try {
	            Optional<Product> productOptional = productService.findById(productId);

	            if (productOptional.isPresent()) {
	                Product product = productOptional.get();
	                Review review = new Review();
	                review.setProduct(product);
	                review.setName(reviewerName);
	                review.setRating(rating);
	                review.setComment(comment);

	                Review savedReview = reviewRepository.save(review);
	                return new ResponseEntity<>(savedReview, HttpStatus.CREATED);
	            } else {
	                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	        }
	    }


	    // Inner class to represent the request body for review submission
	    private static class ReviewRequest {
	        private Long productId;
	        private String reviewerName;
	        private Integer rating;
	        private String comment;

	        // Getters and setters
	        // ...
	    }
}
