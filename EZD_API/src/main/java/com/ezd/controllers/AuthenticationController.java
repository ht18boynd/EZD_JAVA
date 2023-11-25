package com.ezd.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.Dto.JwtAuthenticationResponse;
import com.ezd.Dto.RefreshTokenRequest;
import com.ezd.Dto.Role;
import com.ezd.Dto.SignInRequest;
import com.ezd.Dto.SignUpRequest;
import com.ezd.models.Auth;
import com.ezd.repository.AuthRepository;
import com.ezd.service.AuthenticationService;
import com.ezd.service.PurchaseService;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
	@Autowired
	private AuthRepository authRepository;

	@Autowired
	private AuthenticationService authenticationService;
	
	@Autowired
	private PurchaseService purchaseService;
	
	@Autowired
	private Cloudinary cloudinary;

	@PostMapping("/signup")
	public ResponseEntity<Auth> signup(@RequestBody SignUpRequest signUpRequest) throws MessagingException {
		return ResponseEntity.ok(authenticationService.signup(signUpRequest));
	}

	@PostMapping("/signin")
	public ResponseEntity<JwtAuthenticationResponse> signin(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok(authenticationService.signin(signInRequest));
	}

	@PostMapping("/signinAdmin")
	public ResponseEntity<JwtAuthenticationResponse> signinAdmin(@RequestBody SignInRequest signInRequest) {
		return ResponseEntity.ok(authenticationService.signinAdmin(signInRequest));
	}

	@PostMapping("/refresh")
	public ResponseEntity<JwtAuthenticationResponse> refresh(@RequestBody RefreshTokenRequest refreshTokenRequest) {
		return ResponseEntity.ok(authenticationService.refreshToken(refreshTokenRequest));
	}

	@GetMapping("/findByEmail")
	public ResponseEntity<Optional<Auth>> findByEmail(@RequestParam String email) {
		Optional<Auth> auth = authRepository.findByEmail(email);

		if (auth != null) {
			return ResponseEntity.ok(auth);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	 @GetMapping("/role")
	    public List<Auth> getAllUser(@RequestParam("role") Role role) {
	        return  authRepository.getAllUsersList(role);
	    }
	    @GetMapping("/")
	    public List<Auth> getAllUser() {
	        return  authRepository.findAll();
	    }

	@PostMapping("/reset-password")
	public ResponseEntity<String> resetPassword(@RequestParam String email) {
		try {
			authenticationService.resetPassword(email);
			return ResponseEntity.ok("reset password success");

		} catch (MessagingException e) {
			return ResponseEntity.status(500).body("reset password fail email.");
		}
	}

//	@PostMapping("/{userId}/buyItem")
//	public ResponseEntity<?> buyItem(@RequestParam Long userId, @RequestBody PurchaseRequest request) {
//		try {
//			purchaseService.buyItem(userId, request.getItemId(), request.getQuantity());
//			return ResponseEntity.ok("Item purchased successfully.");
//		} catch (Exception e) {
//			return ResponseEntity.badRequest().body(e.getMessage());
//		}
//	}

	@PostMapping("/updatePassword")
	public ResponseEntity<String> updatePassword(@RequestParam String email, @RequestParam String currentPassword,
			@RequestParam String newPasword) {
		try {
			authenticationService.updatePassword(email, currentPassword, newPasword);
			return ResponseEntity.ok("Password updated successfully");
		} catch (IllegalArgumentException e) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}

	@PutMapping("/editAvatar")
	public ResponseEntity<Auth> updateAvatar(@RequestParam("id") Long id,
			@RequestParam("avatarImages") List<MultipartFile> avatarImages) {
		Optional<Auth> optionalUser = authRepository.findById(id);

		if (optionalUser.isPresent()) {
			Auth user = optionalUser.get();

			try {
				List<String> avatarUrls = new ArrayList<>();

				// Lặp qua từng ảnh và upload lên Cloudinary
				for (MultipartFile avatarImage : avatarImages) {
					if (avatarImage != null) {
						Map uploadResult = cloudinary.uploader().upload(avatarImage.getBytes(), ObjectUtils.emptyMap());
						String imageUrl = (String) uploadResult.get("url");
						avatarUrls.add(imageUrl);
					}
				}

				// Gán danh sách đường dẫn avatars cho người dùng
				user.setAvatars(avatarUrls);

				Auth updatedUser = authRepository.save(user);
				return new ResponseEntity<>(updatedUser, HttpStatus.OK);
			} catch (IOException e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}


}