package com.ezd.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.CloudinaryConfig;

@Service
public class CloudinaryService {
	@Autowired
	private CloudinaryConfig cloudinaryConfig;

	public String uploadImage(MultipartFile file) throws IOException {
		Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
		return (String) uploadResult.get("url");
	}
}
