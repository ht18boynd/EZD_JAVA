package com.ezservice.service.repository;

import org.springframework.data.repository.CrudRepository;

import com.ezservice.service.models.Members;


public interface MemberRepository extends CrudRepository<Members,String> {
	

}
