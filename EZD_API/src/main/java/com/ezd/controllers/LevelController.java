package com.ezd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.ezd.models.Level;
import com.ezd.repository.LevelRepository;

import java.util.List;
import java.util.Optional;


@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/levels")
public class LevelController {

    @Autowired
    private LevelRepository levelRepository; // LevelRepository là một interface dùng để thao tác với cơ sở dữ liệu

    @GetMapping("/")
    public List<Level> getAllLevels() {
        return levelRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Level> getLevelById(@PathVariable Long id) {
        return levelRepository.findById(id);
    }

    @PostMapping("/add")
    public Level createLevel(@RequestParam("name") String name) {
    	Level level = new Level();
    	level.setName(name);
        return levelRepository.save(level);
    }

    @PutMapping("/edit/{id}")
    public Level updateLevel(@PathVariable Long id, @RequestParam("name") String name) {
        Optional<Level> existingLevel = levelRepository.findById(id);
        if (existingLevel.isPresent()) {
            Level level = existingLevel.get();
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
