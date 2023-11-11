package com.ezd.controllers;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.Dto.DataMailDTO;
import com.ezd.service.MailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/mail")
public class MailController {
	@Autowired
	private MailService mailService;
	@PostMapping("/send/{mail}")
	public String sendMail (@PathVariable String mail , @RequestBody DataMailDTO mailStructure) throws MessagingException {
		try {
			mailService.sendHtmlMail(mailStructure, mail, null);
		} catch (MessagingException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "Success";
		
	}

}