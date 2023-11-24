package com.ezd.service;

import java.io.IOException;
import java.math.BigDecimal;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.ezd.models.Rank;
import com.ezd.repository.RankRepository;

@Service
public class RankService {
	@Autowired
	RankRepository rankRepository;
	@Autowired
	private CloudinaryService cloudinaryService;

	List<String> allowExtensions = Arrays.asList("png", "gif", "jpg", "jpeg");

	@Autowired
	public RankService(RankRepository rankRepository) {
		this.rankRepository = rankRepository;
	}

	// Get all ID
	public List<Rank> getAllRanks() {
		return rankRepository.findAll();
	}

	// Get by ID
	public Rank getRankById(Long rankId) {
		return rankRepository.findById(rankId).orElse(null);
	}

	// Delete by Id
	public void deleteRank(Long rankId) {
		rankRepository.deleteById(rankId);
	}

	// Add Rank
	public Rank createRank(Rank rank, MultipartFile adminFrameImage, MultipartFile backgroundImage) throws IOException {

		// Xử lý
		String rankName = rank.getRank_name();
		if (rankName.equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Không tìm thấy giá trị, giá trị không được null");
		} else {
			rank.setRank_name(rankName);
		}
		BigDecimal minimumBalance = rank.getMinimum_balance();
		if (minimumBalance.compareTo(BigDecimal.ZERO) <= 0 || minimumBalance == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Không được null");
		}
		rank.setMinimum_balance(minimumBalance);
		
		BigDecimal maximumBalance = rank.getMaximum_balance();
		if (minimumBalance.compareTo(BigDecimal.ZERO) < 0 || minimumBalance == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"maximumBalance không thể nhỏ hơn 0, hoặc không được null");
		}
		rank.setMaximum_balance(maximumBalance);
		rank.setMinimum_balance(minimumBalance);
		if (!isValidateImageFile(adminFrameImage, allowExtensions)
				|| !isValidateImageFile(backgroundImage, allowExtensions)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đuôi file không hợp lệ. Chỉ chấp nhận các đuôi: " + String.join(", ", allowExtensions));
		} else {
			rank.setAvatar_frame_image(cloudinaryService.uploadImage(adminFrameImage));
			rank.setBackground_image(cloudinaryService.uploadImage(backgroundImage));
		}
		// Lưu đối tượng Rank vào cơ sở dữ liệu
		return rankRepository.save(rank);
	}

	// Edit/update Rank
	public Rank updateRank(Long rankId, Rank updatedRank, MultipartFile adminFrameImage,
			MultipartFile backgroundImage) {

		Rank existingRank = rankRepository.findById(rankId)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Không tìm thấy ID: " + rankId));

		// Xác định lại ID có bằng với ID cần update hay không.
		validateExistingRank(existingRank, rankId);

		// Kiểm tra và set rankName
		String rankName = updatedRank.getRank_name();
		if (rankName.equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "rankName không thể null");
		} else {
			existingRank.setRank_name(rankName);
		}

		// Kiểm tra và set minimumBalance
		BigDecimal minimumBalance = updatedRank.getMinimum_balance();
		if (minimumBalance != null && minimumBalance.compareTo(BigDecimal.ZERO) >= 0) {
			existingRank.setMinimum_balance(minimumBalance);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "minimumBalance không hợp lệ");
		}
		BigDecimal maximumBalance = updatedRank.getMaximum_balance();
		if (minimumBalance.compareTo(BigDecimal.ZERO) < 0 || minimumBalance == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"maximumBalance không thể nhỏ hơn 0, hoặc không được null");
		}
		existingRank.setMaximum_balance(maximumBalance);
		// Kiểm tra điều kiện đuôi file cho phép
		if (!isValidateImageFile(adminFrameImage, allowExtensions)
				|| !isValidateImageFile(backgroundImage, allowExtensions)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đuôi file không hợp lệ. Chỉ chấp nhận các đuôi: " + String.join(", ", allowExtensions));
		}

		// Thực hiện upload hình ảnh
		try {
			existingRank.setAvatar_frame_image(cloudinaryService.uploadImage(adminFrameImage));
			existingRank.setBackground_image(cloudinaryService.uploadImage(backgroundImage));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tải lên ảnh", e);
		}
		return rankRepository.save(existingRank);

	}
	
	// Phương thức xác định hình ảnh
	private boolean isValidateImageFile(MultipartFile imageFile, List<String> allowExtension) {
		String originalFilename = imageFile.getOriginalFilename();
		if (originalFilename != null) {
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
			return allowExtension.contains(extension);
		}
		return false;
	}

	// Xác định ID
	private void validateExistingRank(Rank existingRank, Long rankId) {
		if (!existingRank.getId().equals(rankId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không khớp với ID: " + rankId);
		}
	}
}