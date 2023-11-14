package com.ezd.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
//@AllArgsConstructor
//@NoArgsConstructor
public class DataMailDTO {
    private String subject;
    private String content;
    public DataMailDTO() {
		// TODO Auto-generated constructor stub
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public DataMailDTO(String subject, String content) {
		super();
		this.subject = subject;
		this.content = content;
	}
}
