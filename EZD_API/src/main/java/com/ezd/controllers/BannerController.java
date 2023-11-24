package com.ezd.controllers;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.Dto.BannerStatus;
import com.ezd.models.Banner;
import com.ezd.repository.BannerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/banners")
public class BannerController {

    private final BannerRepository bannerRepository;
    private final Cloudinary cloudinary;

    @Autowired
    public BannerController(BannerRepository bannerRepository, Cloudinary cloudinary) {
        this.bannerRepository = bannerRepository;
        this.cloudinary = cloudinary;
    }

    @GetMapping("/")
    public Iterable<Banner> getAllBanners() {
        return bannerRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Banner> getBannerById(@PathVariable Long id) {
        return bannerRepository.findById(id)
                .map(banner -> new ResponseEntity<>(banner, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PostMapping("/add")
    public ResponseEntity<Banner> createBanner(@RequestParam("name") String name,
            @RequestParam("title") String title, @RequestParam("image") MultipartFile image) {
        try {
            if (name.isEmpty() || title.isEmpty() || image == null) {
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");
            
            Banner banner = new Banner();
            banner.setName(name);
            banner.setTitle(title);
            banner.setImage(imageUrl);
            banner.setStatus(BannerStatus.PENDING);

            Banner savedBanner = bannerRepository.save(banner);
            return new ResponseEntity<>(savedBanner, HttpStatus.CREATED);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/edit/{id}")
    public ResponseEntity<Banner> editBanner(@PathVariable Long id, @RequestParam("name") String name,
            @RequestParam("title") String title, @RequestParam("image") MultipartFile image) {
        Optional<Banner> optionalBanner = bannerRepository.findById(id);
        if (optionalBanner.isPresent()) {
            Banner banner = optionalBanner.get();
            banner.setName(name);
            banner.setTitle(title);

            try {
                if (image != null) {
                    Map uploadResult = cloudinary.uploader().upload(image.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = (String) uploadResult.get("url");
                    banner.setImage(imageUrl);
                }

                Banner updatedBanner = bannerRepository.save(banner);
                return new ResponseEntity<>(updatedBanner, HttpStatus.OK);
            } catch (IOException e) {
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteBanner(@PathVariable Long id) {
        if (bannerRepository.existsById(id)) {
            bannerRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @PutMapping("/changeStatus/{id}")
    public ResponseEntity<Banner> changeBannerStatus(@PathVariable Long id, @RequestParam("status") BannerStatus newStatus) {
        Optional<Banner> optionalBanner = bannerRepository.findById(id);
        if (optionalBanner.isPresent()) {
            Banner banner = optionalBanner.get();
            banner.setStatus(newStatus);

            Banner updatedBanner = bannerRepository.save(banner);
            return new ResponseEntity<>(updatedBanner, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}
