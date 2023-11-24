package com.ezd.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.models.Auth;
import com.ezd.models.Game;
import com.ezd.models.Gender;
import com.ezd.models.LevelGame;
import com.ezd.models.PerfectRole;
import com.ezd.models.Product;
import com.ezd.models.StatusAccount;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.GameRepository;
import com.ezd.repository.GenderRepository;
import com.ezd.repository.LevelRepository;
import com.ezd.repository.PerfectRoleRepository;
import com.ezd.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private AuthRepository authRepository;
    @Autowired
    private PerfectRoleRepository perfectRoleRepository;

    @Autowired
    private LevelRepository levelRepository;

    @Autowired
    private GenderRepository genderRepository;

    @Autowired
    private Cloudinary cloudinary;

    @PostMapping("/add")
    public ResponseEntity<Product> createProduct(@RequestParam("userProductId") Long userProductId,
                                                @RequestParam("gameProductId") Long gameProductId,
                                                @RequestParam("roleProductId") Long roleProductId,
                                                @RequestParam("levelProductId") Long levelProductId,
                                                @RequestParam("genderProductId") Long genderProductId,
                                                @RequestParam("imgProduct") MultipartFile imgProduct,
                                                @RequestParam("price") BigDecimal price,
                                                @RequestParam("hour") int hour,
                                                @RequestParam("description") String description) {
        try {
            if (userProductId == null || gameProductId == null || roleProductId == null || levelProductId == null
                    || genderProductId == null || imgProduct == null || price == null || hour == 0 || description.isEmpty()) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Auth userProduct = authRepository.findById(userProductId).orElse(null);
            Game gameProduct = gameRepository.findById(gameProductId).orElse(null);
            PerfectRole roleProduct = perfectRoleRepository.findById(roleProductId).orElse(null);
            LevelGame levelProduct = levelRepository.findById(levelProductId).orElse(null);
            Gender genderProduct = genderRepository.findById(genderProductId).orElse(null);

            if (userProduct == null || gameProduct == null || roleProduct == null || levelProduct == null || genderProduct == null) {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }

            Map<?, ?> uploadResult = cloudinary.uploader().upload(imgProduct.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            Product product = new Product();
            product.setUser_product(userProduct);
            product.setGame_product(gameProduct);
            product.setRole_product(roleProduct);
            product.setLevel_product(levelProduct);
            product.setGender_product(genderProduct);
            product.setImg_product(imageUrl);
            product.setPrice(price);
            product.setHour(hour);
            product.setDecription(description);
            product.setStatus(StatusAccount.ON);
            product.setCreated_date(new Date());
            // Set other properties as needed

            Product savedProduct = productRepository.save(product);
            return new ResponseEntity<>(savedProduct, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Add other CRUD methods for Product as needed

    @GetMapping("/byUser/{userProductId}")
    public ResponseEntity<List<Product>> getProductsByUser(@PathVariable Long userProductId) {
        try {
            List<Product> products = productRepository.findByUserProductId(userProductId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @GetMapping("/byGame/{gameProductId}")
    public ResponseEntity<List<Product>> getProductsByGame(@PathVariable Long gameProductId) {
        try {
            List<Product> products = productRepository.findByGameProductId(gameProductId);
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/")
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return new ResponseEntity<>(products, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long productId) {
        try {
            productRepository.deleteById(productId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    @PutMapping("/edit/{productId}")
    public ResponseEntity<Product> editProduct(@PathVariable Long productId,
                                               @RequestBody Product updatedProduct) {
        try {
            Optional<Product> existingProductOptional = productRepository.findById(productId);

            if (existingProductOptional.isPresent()) {
                Product existingProduct = existingProductOptional.get();

                // Update properties of existingProduct with the values from updatedProduct
                existingProduct.setGame_product(updatedProduct.getGame_product());
                existingProduct.setRole_product(updatedProduct.getRole_product());
                existingProduct.setLevel_product(updatedProduct.getLevel_product());
                existingProduct.setGender_product(updatedProduct.getGender_product());
                existingProduct.setImg_product(updatedProduct.getImg_product());
                existingProduct.setPrice(updatedProduct.getPrice());
                existingProduct.setHour(updatedProduct.getHour());
                existingProduct.setDecription(updatedProduct.getDecription());

                // Save the updated product
                Product savedProduct = productRepository.save(existingProduct);
                return new ResponseEntity<>(savedProduct, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
