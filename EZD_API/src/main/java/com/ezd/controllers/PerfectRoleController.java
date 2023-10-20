package com.ezd.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.ezd.models.PerfectRole;
import com.ezd.models.PerfectRole;
import com.ezd.repository.PerfectRoleRepository;

@RestController
@RequestMapping("/api/gameroles")
public class PerfectRoleController {
	@Autowired
	private PerfectRoleRepository repository;
	 @GetMapping("/")
	    public List<PerfectRole> getAllRoles() {
	        return repository.findAll();
	    }

	    @GetMapping("/{id}")
	    public Optional<PerfectRole> getPerfecrRoleById(@PathVariable Long id) {
	        return repository.findById(id);
	    }

	    @PostMapping("/add")
	    public PerfectRole createPerfectRole(@RequestParam("name") String roleName) {
	        PerfectRole role = new PerfectRole();
	        role.setName(roleName);
	        return repository.save(role);
	    }


	    @PutMapping("/edit/{id}")
	    public PerfectRole updatePerfectRole(@PathVariable Long id, @RequestParam("name") String name) {
	        Optional<PerfectRole> existingRole = repository.findById(id);
	        if (existingRole.isPresent()) {
	            PerfectRole role = existingRole.get();
	            role.setName(name);
	            return repository.save(role);
	        }
	        return null;
	    }


	    @DeleteMapping("/delete/{id}")
	    public void deletePerfectRole(@PathVariable Long id) {
	    	repository.deleteById(id);
	    }
}
