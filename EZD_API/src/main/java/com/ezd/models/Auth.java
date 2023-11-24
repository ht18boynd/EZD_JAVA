package com.ezd.models;


import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
    
    @ManyToOne
    @JoinColumn(name = "rank_id")
    private Rank currentRank;
    
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

    
    public Rank getCurrentRank() {
		return currentRank;
	}

	public void setCurrentRank(Rank currentRank) {
		this.currentRank = currentRank;
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

    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public StatusAccount getStatus() {
		return status;
	}

	public void setStatus(StatusAccount status) {
		this.status = status;
	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public LocalDateTime getBirthDay() {
		return birthDay;
	}

	public void setBirthDay(LocalDateTime birthDay) {
		this.birthDay = birthDay;
	}

	public LocalDateTime getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDateTime createdDate) {
		this.createdDate = createdDate;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}


	public List<BecomeIdol> getBecomes() {
		return becomes;
	}

	public void setBecomes(List<BecomeIdol> becomes) {
		this.becomes = becomes;
	}

	public List<LuckySpin> getLuckys() {
		return luckys;
	}

	public void setLuckys(List<LuckySpin> luckys) {
		this.luckys = luckys;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
    public Auth() {
		// TODO Auto-generated constructor stub
	}

}
