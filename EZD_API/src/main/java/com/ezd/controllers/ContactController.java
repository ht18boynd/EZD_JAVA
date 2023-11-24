package com.ezd.controllers;


import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ezd.models.Contact;
import com.ezd.repository.ContactRepository;
import com.ezd.service.MailService;

import jakarta.mail.MessagingException;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {
	  @Autowired
	    private MailService mailService;

	private final ContactRepository contactRepository;
	public ContactController(ContactRepository contactRepository) {
        this.contactRepository = contactRepository;
    }
	@GetMapping("/")
    public Iterable<Contact> getAllContacts() {
        return contactRepository.findAll();
    }
	@GetMapping("/{id}")
    public ResponseEntity<Contact> getContactById(@PathVariable Long id) {
        return contactRepository.findById(id)
                .map(contact -> new ResponseEntity<>(contact, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

	@PostMapping("/add")
	public ResponseEntity<Contact> createContact(
	    @RequestParam("fullName") String fullName,
	    @RequestParam("email") String email,
	    @RequestParam("title") String title,
	    @RequestParam("content") String content) {

	    try {
	        if (fullName.isEmpty() || email.isEmpty() || title.isEmpty() || content.isEmpty()) {
	            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
	        }

	        Contact contact = new Contact();
	        contact.setFullName(fullName);
	        contact.setEmail(email);
	        contact.setTitle(title);
	        contact.setContent(content);

	        Contact savedContact = contactRepository.save(contact);
	        return new ResponseEntity<>(savedContact, HttpStatus.CREATED);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}
	
	@PostMapping("/reply")
    public String replyToEmail(
    		 @RequestParam String toEmail,
    	        @RequestParam String subject,
    	        @RequestParam String content
    		) {
       

        try {
            mailService.replyToEmail(toEmail, subject, content);
            return "Email reply sent successfully.";
        } catch (MessagingException | IOException e) {
            e.printStackTrace();
            return "Failed to send email reply.";
        }
	}
}
