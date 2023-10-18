package com.ezd.models;

import org.springframework.web.multipart.MultipartFile;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

@Data
@Entity
@Table(name = "Game")
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idGame;
    
    private String nameGame; // Tên của trò chơi

    // Tên tệp hình ảnh
    private String imageFileName;

    // Loại tệp hình ảnh (nếu cần)
    private String imageFileType;

    // Dữ liệu hình ảnh (mảng byte)
    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] imageData;
    
    @Transient
    private String base64Image; // Thêm một trường để lưu trữ dữ liệu base64

}