package com.ezd.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ezd.models.Game;
import com.ezd.repository.GameService;
@CrossOrigin(origins = "http://localhost:3000/")

@RestController
@RequestMapping("/api/games/")
public class GameController {

    @Autowired
    private GameService serviceRepository;

    @Autowired
    private Cloudinary cloudinary; // Inject Cloudinary bean
    @GetMapping("")
    public List<Game> getAllServices() {
        return serviceRepository.findAll();
    }
    @GetMapping("{id}")
    public ResponseEntity<Game> getServiceById(@PathVariable Long id) {
        Optional<Game> service = serviceRepository.findById(id);
        if (service.isPresent()) {
            return new ResponseEntity<>(service.get(), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @DeleteMapping("delete/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        if (serviceRepository.existsById(id)) {
            serviceRepository.deleteById(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("edit/{id}")
    public ResponseEntity<Game> editService(@PathVariable Long id, @RequestParam("nameGame") String nameGame, @RequestParam("imageName") MultipartFile imageName) {
        Optional<Game> optionalService = serviceRepository.findById(id);
        if (optionalService.isPresent()) {
            Game service = optionalService.get();
            service.setNameGame(nameGame);

            try {
                if (imageName != null) {
                    // Lưu trữ tệp hình ảnh lên Cloudinary và lấy public ID
                    Map uploadResult = cloudinary.uploader().upload(imageName.getBytes(), ObjectUtils.emptyMap());
                    String imageUrl = (String) uploadResult.get("url");
                    service.setImageName(imageUrl);
                }

                Game updatedService = serviceRepository.save(service);
                return new ResponseEntity<>(updatedService, HttpStatus.OK);
            } catch (IOException e) {
                // Xử lý lỗi
                e.printStackTrace();
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("add")
    public Game createService(@RequestParam("nameGame") String nameGame, @RequestParam("imageName") MultipartFile imageName) {
        try {
            // Lưu trữ tệp hình ảnh lên Cloudinary và lấy public ID
            Map uploadResult = cloudinary.uploader().upload(imageName.getBytes(), ObjectUtils.emptyMap());
            String imageUrl = (String) uploadResult.get("url");

            Game service = new Game();
            service.setNameGame(nameGame);
            service.setImageName(imageUrl); // Lưu trữ URL của hình ảnh

            return serviceRepository.save(service);
        } catch (IOException e) {
            // Xử lý lỗi
            e.printStackTrace();
            return null;
        }
    }
}



