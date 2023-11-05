package com.ezd.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ezd.Dto.Role;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Data
@Entity
public class Auth implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private String accountName;
    private String email;
    private String password;
    private String avatar;
    private String address;
    private String country;
    private String phoneNumber;
   
    private String gender;
    private BigDecimal balance;
    private Status status;
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Role role;
    private LocalDateTime birthDay;
    private LocalDateTime createdDate;
    @JsonBackReference

    @OneToMany(mappedBy = "user_transaction")
    private List<Transaction> transactions; // Thêm danh sách giao dịch mà người dùng đã thực hiện

    @JsonBackReference

    @OneToMany(mappedBy = "user_lucky")
    private List<LuckySpin> luckys; // Thêm danh sách  mà người dùng được cộng điểm

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }


    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
