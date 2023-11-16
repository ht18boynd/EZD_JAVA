package com.ezd.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.utils.ObjectUtils;
import com.ezd.CloudinaryConfig;

@Service
public class CloudinaryService {
	@Autowired
	CloudinaryConfig cloudinaryConfig;
	
	public String uploadImage(MultipartFile file) throws IOException {
        Map uploadResult = cloudinaryConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        return (String) uploadResult.get("url");
    }
	
	public List<String> uploadMultipleFile(MultipartFile[] files) {
        return Arrays.stream(files)
                .map(file -> {
                    try {
                        Map<?, ?> uploadResult = cloudinaryConfig.cloudinary().uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
                        return uploadResult.get("url").toString();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return null;
                    }
                })
                .collect(Collectors.toList());
	}
}
