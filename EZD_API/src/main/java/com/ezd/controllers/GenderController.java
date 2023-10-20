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
    private GenderRepository GenderRepository;

    @GetMapping("/")
    public List<Gender> getAllGenders() {
        return GenderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Optional<Gender> getGenderById(@PathVariable Long id) {
        return GenderRepository.findById(id);
    }

    @PostMapping("/add")
    public Gender createGender(@RequestBody Gender Gender) {
        return GenderRepository.save(Gender);
    }

    @PutMapping("/edit/{id}")
    public Gender updateGender(@PathVariable Long id, @RequestBody Gender Gender) {
        if (GenderRepository.existsById(id)) {
            Gender.setId(id);
            return GenderRepository.save(Gender);
        }
        return null;
    }

    @DeleteMapping("/delete/{id}")
    public void deleteGender(@PathVariable Long id) {
        GenderRepository.deleteById(id);
    }
}
