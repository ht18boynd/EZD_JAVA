package com.ezd.controllers;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Game;
import com.ezd.service.GameService;


@RestController
@CrossOrigin(origins = "http://localhost:3000/")

@RequestMapping("/api/games") // Điều chỉnh đường dẫn cơ sở cho API
public class GameController {

    @Autowired
    private GameService gameService;

    @GetMapping("/")
    public List<Game> getAllGames() {
        return gameService.getAllGames();
    }

    @GetMapping("/{id}")
    public Game getGame(@PathVariable int id) {
        return gameService.getGameById(id);
    }

    @PostMapping("/add")
    public Game addGame(@RequestBody Game game) {
        return gameService.save(game);
    }

    @PutMapping("/edit/{id}")
    public Game updateGame(@PathVariable int id, @RequestBody Game game) {
        game.setIdGame(id); // Đảm bảo rằng id của Game không thay đổi
        return gameService.save(game);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGame(@PathVariable int id) {
        gameService.delete(id);
    }
}





