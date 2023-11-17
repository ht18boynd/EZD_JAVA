package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;


import com.ezd.models.Contact;

public interface ContactRepository extends JpaRepository<Contact ,Long >{

}
