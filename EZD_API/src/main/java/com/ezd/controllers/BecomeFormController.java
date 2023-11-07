package com.ezd.controllers;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.Dto.Role;
import com.ezd.Dto.Status;
import com.ezd.models.Auth;
import com.ezd.models.BecomeIdol;
import com.ezd.models.Transaction;
import com.ezd.repository.AuthRepository;
import com.ezd.repository.BecomeRepository;
import com.ezd.service.BecomeService;
import com.ezd.service.TransactionService;

@RestController
@RequestMapping("/api/become-forms")
public class BecomeFormController {
	@Autowired
	private BecomeService becomeService;
	
	@Autowired
	private BecomeRepository becomeRepository;
	
	@Autowired
	private   AuthRepository  authRepository;

@Autowired
    private Cloudinary cloudinary;

    
    @PostMapping("/add")
    public ResponseEntity<BecomeIdol> addBecomeIdol(
            @RequestParam("fullName") String fullName,
            @RequestParam("birthDay") LocalDateTime birthDay,
            @RequestParam("street") String street,
            @RequestParam("imgBefore") MultipartFile imgBefore,
            @RequestParam("imgAfter") MultipartFile imgAfter,
            @RequestParam("imgAvarta") MultipartFile imgAvarta,
            @RequestParam("imgRank") MultipartFile imgRank,
            @RequestParam("decription") String decription,
            @RequestParam("url_faceBook") String urlFaceBook,
            @RequestParam("url_youtube") String urlYoutube,
            @RequestParam("userId") Long userId
    ) throws IOException {
        if (fullName.isEmpty() || imgBefore == null || imgAfter == null || imgAvarta == null || imgRank == null) {
		    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
        Map uploadImageBeforeResult = cloudinary.uploader().upload(imgBefore.getBytes(), ObjectUtils.emptyMap());
        String imgBeforeUrl = (String) uploadImageBeforeResult.get("url");
        Map uploadImageAfterResult = cloudinary.uploader().upload(imgAfter.getBytes(), ObjectUtils.emptyMap());
        String imgAfterUrl = (String) uploadImageAfterResult.get("url");
        
        Map uploadimgAvartaUrlResult = cloudinary.uploader().upload(imgAvarta.getBytes(), ObjectUtils.emptyMap());
        String imgAvartaUrl = (String) uploadimgAvartaUrlResult.get("url");
        
        
        Map uploadimgRankUrlResult = cloudinary.uploader().upload(imgRank.getBytes(), ObjectUtils.emptyMap());
        
        String imgRankUrl = (String) uploadimgRankUrlResult.get("url");


		// Tạo đối tượng BecomeIdol
		BecomeIdol becomeIdol = new BecomeIdol();
		becomeIdol.setFullName(fullName);
		becomeIdol.setBirthDay(birthDay);
		becomeIdol.setStreet(street);
		becomeIdol.setImg_before(imgBeforeUrl);
		becomeIdol.setImg_after(imgAfterUrl);
		becomeIdol.setImg_avarta(imgAvartaUrl);
		becomeIdol.setImg_rank(imgRankUrl);
		becomeIdol.setDecription(decription);
		becomeIdol.setUrl_faceBook(urlFaceBook);
		becomeIdol.setUrl_youtube(urlYoutube);
		becomeIdol.setBecomeTime(LocalDateTime.now());
		becomeIdol.setStatus(Status.PENDING);

		// Tìm người dùng và liên kết thông tin giao dịch với người dùng
		Auth user = authRepository.getById(userId);
		if (user != null) {
		    becomeIdol.setUser_become(user);
		} else {
		    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Lưu thông tin BecomeIdol vào cơ sở dữ liệu
		BecomeIdol savedBecomeIdol = becomeRepository.save(becomeIdol);

		return new ResponseEntity<>(savedBecomeIdol, HttpStatus.CREATED);
    }

    // Phương thức để tải ảnh lên Cloudinary và lấy URL
      
	@PutMapping("/admin-check")
	public void adminCheckTransaction(@RequestParam("id") Long id,
			@RequestParam("newStatus") Status newStatus) {
		becomeService.adminCheckBecome(id, newStatus);
	}
	
	@GetMapping("/byStatus")
    public List<BecomeIdol> getBecomeListByStatus(@RequestParam("status") Status status) {
        return becomeService.getBecomesByStatus(status);
    }
}
