package com.ezportal.portal.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ezservice.service.MemberService;

import com.ezservice.service.models.Members;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/")
public class MemberController {

	@Autowired 
	MemberService service;
	
	@GetMapping("members")
	
	public List<Members> getMembers(){
		return service.getAllMember();
	}
	
	
	
}
