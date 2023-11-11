package com.ezd.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezd.models.Auth;
import com.ezd.models.Game;
import com.ezd.models.Gender;
import com.ezd.models.LevelGame;
import com.ezd.models.PerfectRole;
import com.ezd.models.Product;
import com.ezd.models.StatusAccount;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.GameRepository;
import com.ezd.repository.GenderRepository;
import com.ezd.repository.LevelRepository;
import com.ezd.repository.PerfectRoleRepository;
import com.ezd.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

	@Autowired
	private ProductRepository productRepository;
	@Autowired
    private LevelRepository levelRepository;

    @Autowired
    private PerfectRoleRepository perfectRoleRepository;

    @Autowired
    private GenderRepository genderGameRepository;
	@Autowired
	private GameRepository gameRepository;
	@Autowired
	private AuthRepository authRepository;
	@Autowired
	private Cloudinary cloudinary;

	@GetMapping("/")
	public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
	@PostMapping("/saveProduct")
	public ResponseEntity<Product> saveProduct(
	        @RequestParam("userGameId") Long userGameId,
	        @RequestParam("gameId") Long gameId,
	        @RequestParam("price") BigDecimal price,
	        @RequestParam("decription") String description,
	        @RequestParam("images") List<MultipartFile> images,
	        @RequestParam("roleId") Long roleId,
	        @RequestParam("levelId") Long levelId,
	        @RequestParam("genderId") Long genderId) {

	    try {
	        // Lấy thông tin người dùng và trò chơi từ cơ sở dữ liệu
	        Optional<Auth> optionalUser = authRepository.findById(userGameId);
	        Optional<Game> optionalGame = gameRepository.findById(gameId);

	        if (!optionalUser.isPresent() || !optionalGame.isPresent()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        Auth user = optionalUser.get();
	        Game game = optionalGame.get();

	        // Lấy thông tin về role, level, và gender từ cơ sở dữ liệu
	        Optional<PerfectRole> optionalRole = perfectRoleRepository.findById(roleId);
	        Optional<LevelGame> optionalLevel = levelRepository.findById(levelId);
	        Optional<Gender> optionalGender = genderGameRepository.findById(genderId);

	        if (!optionalRole.isPresent() || !optionalLevel.isPresent() || !optionalGender.isPresent()) {
	            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
	        }

	        PerfectRole role = optionalRole.get();
	        LevelGame level = optionalLevel.get();
	        Gender gender = optionalGender.get();

	        // Tạo sản phẩm mới
	        Product product = new Product();
	        product.setUser_game(user);
	        product.setGame(game);
	        product.setRole(role);
	        product.setLevel(level);
	        product.setGender(gender);
	        product.setStatus(StatusAccount.ON);
	        product.setPrice(price);
	        product.setDecription(description);
	        product.setCreatedProduct(LocalDateTime.now());

	        List<String> imageUrls = new ArrayList<>();

	        for (MultipartFile image : images) {
	            // Tải ảnh lên Cloudinary và lấy URL
	            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
	            String imageUrl = (String) uploadResult.get("url");
	            imageUrls.add(imageUrl);
	        }

	        // Lưu danh sách URL hình ảnh vào sản phẩm
	        product.setImageUrls(imageUrls);

	        // Lưu sản phẩm
	        Product savedProduct = productRepository.save(product);
	        return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
	    } catch (IOException e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}

}
