package com.ezd.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
@Entity
public class Donate {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private BigDecimal amount;
    
    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_from_id", referencedColumnName = "id")
    private Auth user_from; // Tham chiếu đến người dùng

    @JsonManagedReference
    @ManyToOne
    @JoinColumn(name = "user_to_id", referencedColumnName = "id")
    private Auth user_to; // Tham chiếu đến người dùng
    
   
    private LocalDateTime transactionDate;
}
