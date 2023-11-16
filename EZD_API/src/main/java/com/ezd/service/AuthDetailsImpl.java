package com.ezd.service;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.ezd.models.Auth;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class AuthDetailsImpl implements UserDetails {

	private static final long serialVersionUID = 1L;
	private Long id;
	private String username;
	private String accountName;
	private String email;
    private String address;
    private String country;
    private String phoneNumber;
    private String gender;
    private BigDecimal balance;
    
	@JsonIgnore
	private String password;

	private Collection<? extends GrantedAuthority> authorities;
	
	
	public AuthDetailsImpl(Long id,String username, String accountName, String email, String address, String country,
			String phoneNumber, String gender, BigDecimal balance, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.username = username;
		this.accountName = accountName;
		this.email = email;
		this.address = address;
		this.country = country;
		this.phoneNumber = phoneNumber;
		this.gender = gender;
		this.balance = balance;
		this.password = password;
		this.authorities = authorities;
	}
	public static AuthDetailsImpl build(Auth auth) {
		List<GrantedAuthority> authorities = auth.getRoles().stream()
				.map(role -> new SimpleGrantedAuthority(role.getName().name())).collect(Collectors.toList());
		
		return new AuthDetailsImpl(auth.getId()
				, auth.getUsername()
				, auth.getAccountName()
				, auth.getEmail()
				, auth.getAddress()
				, auth.getCountry()
				, auth.getPhoneNumber()
				, auth.getGender()
				, auth.getBalance()
				, auth.getPassword()
				, authorities);
	}
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		// TODO Auto-generated method stub
		return authorities;
	}
	
	public Long getId() {
		return id;
	}
	public String getAccountName() {
		return accountName;
	}
	public String getEmail() {
		return email;
	}
	public String getAddress() {
		return address;
	}
	public String getCountry() {
		return country;
	}
	public String getPhoneNumber() {
		return phoneNumber;
	}
	public String getGender() {
		return gender;
	}
	public BigDecimal getBalance() {
		return balance;
	}
	@Override
	public String getPassword() {
		// TODO Auto-generated method stub
		return password;
	}

	@Override
	public String getUsername() {
		// TODO Auto-generated method stub
		return username;
	}

	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean isEnabled() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public int hashCode() {
		return Objects.hash(accountName, email, id, username);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AuthDetailsImpl other = (AuthDetailsImpl) obj;
		return Objects.equals(accountName, other.accountName) && Objects.equals(email, other.email)
				&& Objects.equals(id, other.id) && Objects.equals(username, other.username);
	}
	
}
