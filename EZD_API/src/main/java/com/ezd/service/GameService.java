package com.ezd.service;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezd.models.Game;
import com.ezd.repository.IGame;

@Service("GameService")

public class GameService {
	@Autowired
    private IGame repository;

    public List<Game> getAllGames() {
        List<Game> list = new ArrayList<>();
        Iterable<Game> result = repository.findAll();
        result.forEach(list::add);
        return list;
    }

    public Game getGameById(int id) {
        return repository.findById(id).orElse(null);
    }

    public Game save(Game game) {
        // Thực hiện kiểm tra logic kinh doanh trước khi lưu game;
        return repository.save(game);
    }

    public void delete(int id) {
        // Thực hiện kiểm tra logic kinh doanh trước khi xóa game;
        repository.deleteById(id);
    }
}
