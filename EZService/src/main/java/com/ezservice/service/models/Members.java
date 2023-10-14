package com.ezservice.service.models;



import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name="members")
public class Members {
	
	@Id
	private String id ; 
	
	private String name; 
	
	private String email;

}
