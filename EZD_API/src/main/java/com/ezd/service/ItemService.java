package com.ezd.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.models.Item;
import com.ezd.repository.ItemRespository;

@Service
public class ItemService {

	@Autowired
	ItemRespository itemRepository;

	@Autowired
	CloudinaryService cloudinaryService;
	@Autowired
	Cloudinary cloudinary;
	
	List<String> allowExtensions = Arrays.asList("png", "gif", "jpg", "jpeg");

	@Autowired
	public ItemService(ItemRespository itemRespository) {
		this.itemRepository = itemRespository;
	}

	// Get All Item
	public List<Item> getAllItems() {
		return itemRepository.findAll();
	}

	// Get Item by ID
	public Optional<Item> getItemById(Long itemId) {
		return itemRepository.findById(itemId);
	}

	// Delete Item
	public void deleteItem(Long itemId) {
		itemRepository.deleteById(itemId);
	}

	// Add multiple
	public List<Item> createMultipleItems(List<Item> items, List<MultipartFile> imageFiles) throws IOException {
		List<Item> savedItems = new ArrayList<>();

		for (int i = 0; i < items.size(); i++) {
			Item item = items.get(i);
			MultipartFile imageFile = imageFiles.get(i);

			String itemName = item.getName();
			BigDecimal itemPrice = item.getPrice();

			if (itemName == null || itemName.isEmpty()) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemName không thể null hoặc trống");
			}
			if (itemPrice == null || itemPrice.compareTo(BigDecimal.ZERO) < 0) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"itemPrice không thể nhỏ hơn 0, hoặc không được null");
			}
			if (!isValidateImageFile(imageFile, allowExtensions)) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
						"Đuôi file không hợp lệ. Chỉ chấp nhận các đuôi: " + String.join(", ", allowExtensions));
			}

			Item newItem = new Item();
			newItem.setName(itemName);
			newItem.setPrice(itemPrice);
			newItem.setImageUrl(cloudinaryService.uploadImage(imageFile));

			savedItems.add(newItem);
		}

		try {
			return itemRepository.saveAll(savedItems);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi lưu nhiều items", e);
		}
	}
	//Add Item
	public Item saveItem(Item item, MultipartFile imageFile) throws IOException {
		// Xử lý các itemName trùng tên hoặc đã tồn tại

		//
		String itemName = item.getName();
		BigDecimal itemPrice = item.getPrice();
		Map uploadResult = cloudinary.uploader().upload(imageFile.getBytes(), ObjectUtils.emptyMap());
        String imageUrl = (String) uploadResult.get("url");
        
		if (itemName.equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemName không thể null");
		}
		if (itemPrice.compareTo(BigDecimal.ZERO) < 0 || itemPrice == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"itemPrice không thể nhỏ hơn 0, hoặc không được null");
		}
		if (!isValidateImageFile(imageFile, allowExtensions)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đuôi file không hợp lệ. Chỉ chấp nhận các đuôi: " + String.join(", ", allowExtensions));
		}

		
		item.setName(itemName);
		item.setPrice(itemPrice);
		item.setImageUrl(imageUrl);
		// Save item to database
		return itemRepository.save(item);
	}

	// Edit Item
	public Item updateItem(Long itemId, Item updateItem, MultipartFile imageFile) {

		Item existingItem = itemRepository.findById(itemId).orElseThrow(
				() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Không tìm thấy ID: " + itemId));

		validateExistingRank(existingItem, itemId);

		String itemName = updateItem.getName();
		if (itemName.equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "itemName không thể null");
		} else {
			existingItem.setName(itemName);
		}

		BigDecimal itemPrice = updateItem.getPrice();
		if (itemPrice.compareTo(BigDecimal.ZERO) > 0) {
			existingItem.setPrice(itemPrice);
		} else {
			throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "itemPrice không hợp lệ");
		}

		if (!isValidateImageFile(imageFile, allowExtensions)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"Đuôi file không hợp lệ. Chỉ chấp nhận các đuôi: " + String.join(", ", allowExtensions));
		}
		try {
			existingItem.setImageUrl(cloudinaryService.uploadImage(imageFile));
		} catch (IOException e) {
			e.printStackTrace();
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Lỗi khi tải lên ảnh", e);
		}

		return itemRepository.save(existingItem);

	}

	// Xác thực hình đuôi hình ảnh.
	private boolean isValidateImageFile(MultipartFile imageFile, List<String> allowExtension) {
		String originalFilename = imageFile.getOriginalFilename();
		if (originalFilename != null) {
			String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
			return allowExtension.contains(extension);
		}
		return false;
	}

	// Xác thực ID với ID đã tìm kím
	private void validateExistingRank(Item existingItem, Long itemId) {
		if (!existingItem.getId().equals(itemId)) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Không khớp với ID: " + itemId);
		}
	}
	
	private void validateItem(Item item) {
		
		String itemName = item.getName();
		if (itemName.equals("")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "itemName không thể null");
		}else {
			item.setName(itemName);
		}
		
		BigDecimal itemPrice = item.getPrice();
		if (itemPrice.compareTo(BigDecimal.ZERO) < 0 || itemPrice == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
					"itemPrice không thể nhỏ hơn 0, hoặc không được null");
		}else {
			item.setPrice(itemPrice);
		}
	}
	
//    private List<Item> mapJsonToItems(String items) {
//        // Logic to map JSON string to list of Item objects
//        // Use a JSON library like Jackson or Gson
//        // Here's an example using Jackson:
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            return objectMapper.readValue(items, new TypeReference<List<Item>>() {});
//        } catch (JsonProcessingException e) {
//            e.printStackTrace();
//            return Collections.emptyList();
//        }
//    }
}