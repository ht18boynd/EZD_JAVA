package com.ezd.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Feedback;
import com.ezd.repository.FeedbackRepository;

@RestController
@RequestMapping("/api/feedbacks")
public class FeedbackController {
	private final FeedbackRepository feedbackRepository;

    @Autowired
    public FeedbackController(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

 // Trong FeedbackController
    @GetMapping("/")
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        try {
            List<Feedback> feedbacks = feedbackRepository.findAll();

            if (feedbacks.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return new ResponseEntity<>(feedbacks, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @PostMapping("/submit")
    public ResponseEntity<Feedback> submitFeedback(
            @RequestParam("rating") Integer rating,
            @RequestParam("comment") String comment,
            @RequestParam("userName") String userName) {
        try {
            // Kiểm tra các trường bắt buộc
            if (rating == null || comment == null || userName == null) {
                return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
            }

            // Tạo đối tượng Feedback từ tham số
            Feedback feedback = new Feedback();
            feedback.setRating(rating);
            feedback.setComment(comment);
            feedback.setUserName(userName);

            // Lưu phản hồi vào database
            Feedback savedFeedback = feedbackRepository.save(feedback);

            return new ResponseEntity<>(savedFeedback, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
