package com.ezd.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.ezd.ResourceNotFoundException;
import com.ezd.models.Game;
import com.ezd.models.LevelGame;
import com.ezd.service.LevelService;

@Controller
@RequestMapping("api/levels")
public class LevelController {
	
    @Autowired
    private LevelService levelService;

    @GetMapping
    public List<LevelGame> getAllLevels() {
        return levelService.getAllLevels();
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<LevelGame> getLevelById(@PathVariable Long id) {
    	try {
            LevelGame levelGame = levelService.getLevelById(id);
//            return ResponseEntity.ok().body(levelGame);
            return new ResponseEntity<>(levelGame,HttpStatus.OK);
        } catch (ResourceNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<LevelGame> createLevel(@RequestBody LevelGame levelGame) {
    	
        LevelGame createdLevel = levelService.createLevel(levelGame);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(createdLevel.getIdLevel()).toUri();
        return ResponseEntity.created(location).body(createdLevel);
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<LevelGame> updateLevel(@PathVariable Long id, @RequestBody LevelGame levelGame) {
        
		try {
			LevelGame updatedLevel = levelService.updateLevel(id, levelGame);
			return new ResponseEntity<>(updatedLevel,HttpStatus.OK);
		} catch (ResourceNotFoundException e) {
			 return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
        
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteLevel(@PathVariable Long id) {
        levelService.deleteLevel(id);
        return ResponseEntity.noContent().build();
    }
	
}
