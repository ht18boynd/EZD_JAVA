package com.ezd.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ezd.Dto.DataMailDTO;


@Service
public class MailService {
	@Autowired
	private  JavaMailSender javaMailSender;
	@Value("$(spring.mail.username)")
	private String fromMail;
	 @Autowired
	    private SpringTemplateEngine templateEngine;
   public  void sendHtmlMail(DataMailDTO dataMail, String mail) {
    	SimpleMailMessage mailMessage  = new SimpleMailMessage();
    	mailMessage.setFrom(fromMail);
    	mailMessage.setSubject(dataMail.getSubject());
    	mailMessage.setText(dataMail.getContent());
    	mailMessage.setTo(mail);
    	
    	javaMailSender.send(mailMessage);
    }
}
