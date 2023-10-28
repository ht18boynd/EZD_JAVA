package com.ezd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezd.models.LevelGame;
import com.ezd.repository.LevelRepository;

import java.util.List;
import java.util.Optional;


@CrossOrigin()



@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @Autowired
    private LevelRepository levelRepository; // LevelRepository là một interface dùng để thao tác với cơ sở dữ liệu

    @GetMapping("/")
    public List<LevelGame> getAllLevels() {
        return levelRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<LevelGame> getLevelById(@PathVariable Long id) {
        return levelRepository.findById(id);
    }

    @PostMapping("/add")
    public LevelGame createLevel(@RequestParam("name") String name) {
    	LevelGame level = new LevelGame();
    	level.setName(name);
        return levelRepository.save(level);
    }

    @PutMapping("/edit/{id}")
    public LevelGame updateLevel(@PathVariable Long id, @RequestParam("name") String name) {
        Optional<LevelGame> existingLevel = levelRepository.findById(id);
        if (existingLevel.isPresent()) {
        	LevelGame level = existingLevel.get();
            level.setName(name);
            return levelRepository.save(level);
        }
        return null;
    }


    @DeleteMapping("/delete/{id}")
    public void deleteLevel(@PathVariable Long id) {
        levelRepository.deleteById(id);
    }
}
