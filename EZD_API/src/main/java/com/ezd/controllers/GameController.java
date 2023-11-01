package com.ezd.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.models.Game;
import com.ezd.models.Gender;
import com.ezd.models.LevelGame;
import com.ezd.models.PerfectRole;
import com.ezd.repository.GameRepository;
import com.ezd.repository.GenderRepository;
import com.ezd.repository.LevelRepository;
import com.ezd.repository.PerfectRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/games")
public class GameController {

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private PerfectRoleRepository perfectRoleRepository;

    @Autowired
    private GenderRepository genderGameRepository;

    @Autowired
    private Cloudinary cloudinary;

    @GetMapping("/")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Game> getGameById(@PathVariable Long id) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            return new ResponseEntity<>(optionalGame.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGame(@PathVariable Long id) {
        if (gameRepository.existsById(id)) {
            gameRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<Game> createGame(@RequestParam("nameGame") String nameGame,
            @RequestParam("imageName") MultipartFile imageName, @RequestParam("levelIds") List<Long> levelIds,
            @RequestParam("roleIds") List<Long> roleIds, @RequestParam("genderIds") List<Long> genderIds) {
        try {
            if (nameGame.isEmpty() || imageName == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);

            }

            Map uploadResult = cloudinary.uploader().upload(imageName.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            Game game = new Game();
            game.setNameGame(nameGame);
            game.setImageName(imageUrl);

            List<LevelGame> levels = (List<LevelGame>) levelRepository.findAllById(levelIds);
            game.setLevels(levels);

            List<PerfectRole> roles = (List<PerfectRole>) perfectRoleRepository.findAllById(roleIds);
            game.setRoles(roles);

            List<Gender> genders = (List<Gender>) genderGameRepository.findAllById(genderIds);
            game.setGenders(genders);

            Game savedGame = gameRepository.save(game);
            return new ResponseEntity<>(savedGame, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}/name")
    public ResponseEntity<Game> updateGameName(@PathVariable Long id, @RequestParam("nameGame") String nameGame) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            game.setNameGame(nameGame);
            Game updatedGame = gameRepository.save(game);
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/edit/{id}/image")
    public ResponseEntity<Game> updateGameImage(@PathVariable Long id, @RequestParam("imageName") MultipartFile imageName) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            try {
                if (imageName != null) {
                    Map uploadResult = cloudinary.uploader().upload(imageName.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = (String) uploadResult.get("url");
                    game.setImageName(imageUrl);
                }
                Game updatedGame = gameRepository.save(game);
                return new ResponseEntity<>(updatedGame, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }  
    
    @PutMapping("/edit/{id}/roles")
    public ResponseEntity<Game> updateGameRoles(@PathVariable Long id, @RequestParam("roleIds") List<Long> roleIds) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            List<PerfectRole> roles = (List<PerfectRole>) perfectRoleRepository.findAllById(roleIds);
            game.setRoles(roles);
            Game updatedGame = gameRepository.save(game);
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Chỉnh sửa giới tính của trò chơi
    @PutMapping("/edit/{id}/genders")
    public ResponseEntity<Game> updateGameGenders(@PathVariable Long id, @RequestParam("genderIds") List<Long> genderIds) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            List<Gender> genders = (List<Gender>) genderGameRepository.findAllById(genderIds);
            game.setGenders(genders);
            Game updatedGame = gameRepository.save(game);
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Chỉnh sửa level của trò chơi
    @PutMapping("/edit/{id}/levels")
    public ResponseEntity<Game> updateGameLevels(@PathVariable Long id,@RequestParam("levelIds") List<Long> levelIds) {
        Optional<Game> optionalGame = gameRepository.findById(id);
        if (optionalGame.isPresent()) {
            Game game = optionalGame.get();
            List<LevelGame> levels = (List<LevelGame>) levelRepository.findAllById(levelIds);
            game.setLevels(levels);
            Game updatedGame = gameRepository.save(game);
            return new ResponseEntity<>(updatedGame, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

   
}

