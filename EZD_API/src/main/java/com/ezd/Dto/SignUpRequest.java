package com.ezd.Dto;


import com.ezd.models.Role;
import com.ezd.models.Status;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class SignUpRequest {
    private String name;
    private String accountName;
    private String email;
    private String password;
    private String address;
    private String country;
    private String phoneNumber;
    private String gender;
    private BigDecimal balance;
    private Status status;
    private Role role;
}
