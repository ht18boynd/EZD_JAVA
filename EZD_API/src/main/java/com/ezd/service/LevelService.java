package com.ezd.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezd.ResourceNotFoundException;
import com.ezd.models.LevelGame;
import com.ezd.repository.LevelRepository;

@Service
public class LevelService {
	
	@Autowired
	LevelRepository levelRepository;
	
    public List<LevelGame> getAllLevels() {
        return levelRepository.findAll();
    }

    public LevelGame getLevelById(Long id) throws ResourceNotFoundException {
    	
        return levelRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("LevelGame not found with id " + id));
    }

    public LevelGame createLevel(LevelGame levelGame) {
    	
        return levelRepository.save(levelGame);
    }

    public LevelGame updateLevel(Long id, LevelGame levelGame) throws ResourceNotFoundException {
        LevelGame existingLevel = getLevelById(id);
        existingLevel.setLevelValue(levelGame.getLevelValue());
        existingLevel.setGame(levelGame.getGame());
        return levelRepository.save(existingLevel);
    }

    public void deleteLevel(Long id) {
        levelRepository.deleteById(id);
    }
}
