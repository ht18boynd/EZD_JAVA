package com.ezd.service;

import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import com.ezd.Dto.DataMailDTO;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@Service
public class MailService {
	@Autowired
	private  JavaMailSender javaMailSender;
	@Value("${spring.mail.username}")
	private String fromMail;
	 @Autowired
	    private SpringTemplateEngine templateEngine;

   public void sendHtmlMail(DataMailDTO mailStructure, String toEmail, Map<String, Object> model) 
           throws MessagingException, IOException {
       MimeMessage mimeMessage = javaMailSender.createMimeMessage();
       MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
       
       // Set thông tin người nhận, người gửi, và tiêu đề
       helper.setTo(toEmail);
       helper.setSubject(mailStructure.getSubject());
       helper.setFrom(fromMail); // Đặt email của bạn ở đây

       // Xử lý template Thymeleaf
       Context context = new Context();
       context.setVariables(model);
       String htmlContent = templateEngine.process("email-template", context);

       helper.setText(htmlContent, true);

       javaMailSender.send(mimeMessage);
   }
}
