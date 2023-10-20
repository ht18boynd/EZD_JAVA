package com.ezd.controllers;

import com.ezd.models.Gender;
import com.ezd.repository.GenderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000")

@RestController
@RequestMapping("/api/genders")
public class GenderController {

    @Autowired
    private GenderRepository genderRepository;

    @GetMapping("/")
    public List<Gender> getAllGenders() {
        return genderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Gender> getGenderById(@PathVariable Long id) {
        return genderRepository.findById(id);
    }

    @PostMapping("/add")
    public Gender createGender(@RequestParam("name") String name) {
    	Gender gender = new Gender();
    	gender.setName(name);
        return genderRepository.save(gender);
    }

    @PutMapping("/edit/{id}")
    public Gender updateGender(@PathVariable Long id, @RequestParam("name") String name) {
        Optional<Gender> existingGender = genderRepository.findById(id);
        if (existingGender.isPresent()) {
            Gender gender = existingGender.get();
            gender.setName(name);
            return genderRepository.save(gender);
        }
        return null;
    }


    @DeleteMapping("/delete/{id}")
    public void deleteGender(@PathVariable Long id) {
    	genderRepository.deleteById(id);
    }
}
