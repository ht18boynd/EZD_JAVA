package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.QuizItem;

public interface QuizItemRepository extends JpaRepository<QuizItem, Long> {

}
