package com.ezd.Dto;


import lombok.Data;

@Data
public class RefreshTokenRequest {
    private String token;
    public RefreshTokenRequest() {
		// TODO Auto-generated constructor stub
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public RefreshTokenRequest(String token) {
		super();
		this.token = token;
	}
	

}
