package com.ezd.models;


import jakarta.persistence.ElementCollection;
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
    private String email;
    private String password;
    @ElementCollection
    private List<String> avatars;
    private String address;
    private String country;
    private String phoneNumber;
    private String gender;
    private BigDecimal balance = BigDecimal.ZERO;
    private StatusAccount status;
    
    @JsonInclude(JsonInclude.Include.ALWAYS)
    private Role role;
    private LocalDateTime birthDay;
    private LocalDateTime createdDate;
    
    @JsonBackReference
    @OneToMany(mappedBy = "user_transaction")
    private List<Transaction> transactions; // Thêm danh sách giao dịch mà người dùng đã thực hiện

    @JsonBackReference
    @OneToMany(mappedBy = "user_from")
    private List<Donate> donationsFrom; // Thêm danh sách donate từ người gửi
    @JsonBackReference
    @OneToMany(mappedBy = "user_to")
    private List<Donate> donationsTo; // Thêm danh sách donate đến người nhận

   
    
    @JsonBackReference
    @OneToMany(mappedBy = "user_product")
    private List<Product> products ;
    
    @JsonBackReference
    @OneToMany(mappedBy = "user_become")
    private List<BecomeIdol> becomes ;


    @JsonBackReference

    @OneToMany(mappedBy = "user_lucky")
    private List<LuckySpin> luckys; //
    
   

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
