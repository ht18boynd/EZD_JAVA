package com.ezd.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ezd.models.QuizItem;
import com.ezd.repository.QuizItemRepository;

@RestController
@RequestMapping("/api/quizitems")
public class QuizItemController {
	@Autowired
	public QuizItemRepository quizItemRepository;
	@GetMapping("/")
    public List<QuizItem> getAllQuizItems() {
        return quizItemRepository.findAll();
    }

	@PostMapping("/add")
	public void addNewQuizItem(
	        @RequestParam String question,
	        @RequestParam List<String> answers,
	        @RequestParam int correctAnswerIndex
	) {
		answers.replaceAll(answer -> answer.replaceAll("[\"\\[\\]]", ""));
	    QuizItem quizItem = new QuizItem();
	    quizItem.setQuestion(question);
	    quizItem.setAnswers(answers);
	    quizItem.setCorrectAnswerIndex(correctAnswerIndex);

	    quizItemRepository.save(quizItem);
	}
	@DeleteMapping("/delete")
    public void deleteQuizItem(@RequestParam Long id) {
        quizItemRepository.deleteById(id);
    }
	@PutMapping("/update")
    public void updateQuizItem(
            @RequestParam Long id,
            @RequestParam String question,
            @RequestParam List<String> answers,
            @RequestParam int correctAnswerIndex
    ) {
		
        Optional<QuizItem> optionalQuizItem = quizItemRepository.findById(id);
        if (optionalQuizItem.isPresent()) {
            QuizItem quizItem = optionalQuizItem.get();
            quizItem.setQuestion(question);
            quizItem.setAnswers(answers);
            quizItem.setCorrectAnswerIndex(correctAnswerIndex);

            quizItemRepository.save(quizItem);
        } else {
            // Xử lý khi không tìm thấy QuizItem với id tương ứng
            // Có thể throw exception, trả về HTTP 404, hoặc làm điều gì đó phù hợp với yêu cầu của bạn
        }
    }


}
