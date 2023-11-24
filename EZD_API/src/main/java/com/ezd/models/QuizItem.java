package com.ezd.models;

import java.util.List;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;

@Entity
public class QuizItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String question;

	@ElementCollection
	@CollectionTable(name = "answers", joinColumns = @JoinColumn(name = "quiz_item_id"))
	private List<String> answers;

	private int correctAnswerIndex;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public List<String> getAnswers() {
		return answers;
	}

	public void setAnswers(List<String> answers) {
		this.answers = answers;
	}

	public int getCorrectAnswerIndex() {
		return correctAnswerIndex;
	}

	public void setCorrectAnswerIndex(int correctAnswerIndex) {
		this.correctAnswerIndex = correctAnswerIndex;
	}

	// Constructors, getters, and setters

}
