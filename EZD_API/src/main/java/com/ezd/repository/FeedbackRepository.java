package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long>{

}
