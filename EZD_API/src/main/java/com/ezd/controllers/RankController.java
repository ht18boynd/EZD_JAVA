package com.ezd.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
//import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezd.models.Rank;
import com.ezd.service.CloudinaryService;
//import com.ezd.repository.RankRepository;
import com.ezd.service.RankService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/ranks")
//@CrossOrigin(origins = "http://localhost:8000/")
public class RankController {

	@Autowired
	private RankService rankService;

	@GetMapping("/")
	public ResponseEntity<List<Rank>> getAllRanks() {
		List<Rank> ranks = rankService.getAllRanks();
		return new ResponseEntity<>(ranks, HttpStatus.OK);
	}

	@GetMapping("/{rankId}")
	public ResponseEntity<Rank> getRankById(@PathVariable Long rankId) {
		Rank rank = rankService.getRankById(rankId);
		if (rank != null) {
			return new ResponseEntity<>(rank, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public ResponseEntity<Rank> createRank(@Valid @ModelAttribute Rank rank,
			@RequestPart("adminFrameImage") MultipartFile avatarFrameFrameImage,
			@RequestPart("backgroundImage") MultipartFile backgroundImage) {
		Rank createdRank;
		try {
			createdRank = rankService.createRank(rank, avatarFrameFrameImage, backgroundImage);
			return new ResponseEntity<>(createdRank, HttpStatus.OK);

		} catch (IOException e) {

			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.BAD_GATEWAY);
		}
	}

	@RequestMapping(value = "/edit/{rankId}", method = RequestMethod.PUT)
	public ResponseEntity<Rank> updateRank(@PathVariable Long rankId, @ModelAttribute Rank rank,
			@RequestPart(value = "adminFrameImage") MultipartFile avatarFrameFrameImage,
			@RequestPart(value = "backgroundImage") MultipartFile backgroundImage) {
		Rank updateRank;
		if (rankService.getRankById(rankId) != null) {
			updateRank = rankService.updateRank(rankId, rank, avatarFrameFrameImage, backgroundImage);
			return new ResponseEntity<>(updateRank, HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@RequestMapping(value = "/delete/{rankId}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> deleteRank(@PathVariable Long rankId) {
		if (rankService.getRankById(rankId) != null) {
			rankService.deleteRank(rankId);
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

	}
}
