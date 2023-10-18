package com.ezd.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Game;
import com.ezd.payload.Response;
import com.ezd.service.GameService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000/")
@RequestMapping("/api/games")
public class GameController {

	@Autowired
	private GameService gameService;
	@GetMapping("/")
	public List<Game> getAllGames() {
	    List<Game> games = gameService.getAllGames();
	    for (Game game : games) {
	        game.setBase64Image(Base64.getEncoder().encodeToString(game.getImageData()));
	    }
	    return games;
	}

	  @DeleteMapping("/delete/{id}")
	    public void deleteGame(@PathVariable Long id) {
	        gameService.delete(id);
	    }


	@PostMapping("/uploadFile")
	public Response uploadFile(@RequestParam("file") MultipartFile file, @RequestParam("nameGame") String nameGame) {
	    Game game = gameService.storeGame(file, nameGame);

	    String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
	            .path("/downloadFile/")
	            .path(game.getImageFileName())
	            .toUriString();

	    return new Response(game.getImageFileName(), fileDownloadUri,
	            file.getContentType(), file.getSize());
	}
	
	@PostMapping("/edit/{id}")
	public ResponseEntity<String> editGame(@PathVariable Long id, @RequestParam("file") MultipartFile file, @RequestParam("nameGame") String nameGame) {
	    // Lấy trò chơi cần chỉnh sửa từ dự liệu
	    Game game = gameService.getGame(id);

	    if (game == null) {
	        return new ResponseEntity<>("Game not found", HttpStatus.NOT_FOUND);
	    }

	    // Gọi phương thức cập nhật từ service
	    Game updatedGame = gameService.updateGame(id, file, nameGame);

	    if (updatedGame == null) {
	        return new ResponseEntity<>("Update failed", HttpStatus.INTERNAL_SERVER_ERROR);
	    }

	    return new ResponseEntity<>("Game updated successfully", HttpStatus.OK);
	}

	
	@GetMapping("/downloadFile/{fileName:.+}")
	public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
	    // Try to parse the fileName as a Long (assuming it's a game ID)
	    Long gameId = Long.parseLong(fileName);

	    // Attempt to get the game by ID (assuming imageFileName is not unique)
	    Game game = gameService.getGame(gameId);

	    if (game == null) {
	        // Handle the case where the Game is not found
	        return ResponseEntity.notFound().build();
	    }

	    // Create a ByteArrayResource from the imageData of the Game
	    ByteArrayResource resource = new ByteArrayResource(game.getImageData());

	    return ResponseEntity.ok()
	        .contentType(MediaType.parseMediaType(game.getImageFileType()))
	        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + game.getImageFileName() + "\"")
	        .body(resource);
	}
    
}
