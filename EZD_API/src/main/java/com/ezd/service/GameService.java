package com.ezd.service;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ezd.exception.FileNotFoundException;
import com.ezd.exception.FileStorageException;
import com.ezd.models.Game;
import com.ezd.repository.IGame;
import org.springframework.util.StringUtils;

@Service("GameService")

public class GameService {
	@Autowired
    private IGame repository;

	public Game storeGame(MultipartFile imageFile, String nameGame) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if (fileName.contains("..")) {
            	throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            byte[] imageData = imageFile.getBytes();

            Game game = new Game();
            game.setNameGame(nameGame);
            game.setImageFileName(fileName);
            game.setImageFileType(imageFile.getContentType());
            game.setImageData(imageData);

            return repository.save(game);
        } catch (IOException ex) {
            throw new FileStorageException("Could not store game " + fileName + ". Please try again!", ex);
        }
    }

	
	public Game updateGame(Long gameId, MultipartFile imageFile, String nameGame) {
	    // Kiểm tra xem trò chơi có tồn tại dựa trên ID hay không
	    Game existingGame = repository.findById(gameId)
	            .orElseThrow(() -> new FileNotFoundException("Game not found with id " + gameId));

	    // Normalize file name
	    String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());

	    try {
	        // Check if the file's name contains invalid characters
	        if (fileName.contains("..")) {
	            throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
	        }

	        byte[] imageData = imageFile.getBytes();

	        // Cập nhật thông tin của trò chơi
	        existingGame.setNameGame(nameGame);
	        existingGame.setImageFileName(fileName);
	        existingGame.setImageFileType(imageFile.getContentType());
	        existingGame.setImageData(imageData);

	        return repository.save(existingGame);
	    } catch (IOException ex) {
	        throw new FileStorageException("Could not update game " + fileName + ". Please try again!", ex);
	    }
	}

    public Game getGame(Long gameId) {
        return repository.findById(gameId)
                .orElseThrow(() -> new FileNotFoundException("Game not found with id " + gameId));
    }
    
    public List<Game> getAllGames() {
        List<Game> list = new ArrayList<>();
        Iterable<Game> result = repository.findAll();
        result.forEach(game -> {
            game.setBase64Image(Base64.getEncoder().encodeToString(game.getImageData()));
            list.add(game);
        });
        return list;
    }
    
    public void delete(Long id) {
        repository.deleteById(id);
    }
}
