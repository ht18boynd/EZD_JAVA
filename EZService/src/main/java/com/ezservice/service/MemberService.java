package com.ezservice.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ezservice.service.models.Members;
import com.ezservice.service.repository.MemberRepository;

@Service("EZService")
public class MemberService {

	@Autowired
	MemberRepository repository ; 
	
	public List<Members> getAllMember(){
		List<Members> list = new ArrayList<>();
		Iterable<Members> result = repository.findAll();
		
		result.forEach(list::add);
		
		return list;
	}
}
