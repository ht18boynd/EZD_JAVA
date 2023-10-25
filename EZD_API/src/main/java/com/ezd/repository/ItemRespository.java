package com.ezd.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ezd.models.Item;

public interface ItemRespository extends JpaRepository<Item, Long> {

}
