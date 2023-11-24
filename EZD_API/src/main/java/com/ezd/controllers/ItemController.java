package com.ezd.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ezd.models.Item;
import com.ezd.service.ItemService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/items")
public class ItemController {
	
	@Autowired
	private ItemService itemService;
	
	@GetMapping("/")
    public List<Item> getAllItems() {
        return itemService.getAllItems();
    }
	
    @GetMapping("/{itemId}")
    public ResponseEntity<Item> getItemById(@PathVariable Long itemId) {
        Optional<Item> item = itemService.getItemById(itemId);
        return item.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
	
    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public ResponseEntity<Item> saveItem(@Valid @ModelAttribute Item item, @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        Item savedItem = itemService.saveItem(item, imageFile);
        return ResponseEntity.ok(savedItem);
    }
    
    @RequestMapping(value = "/add-multiple",method = RequestMethod.POST)
    public ResponseEntity<List<Item>> createMultipleItems(@Valid @ModelAttribute ArrayList<Item> items,
    		@RequestPart("imageFile") List<MultipartFile> files) throws IOException {
        List<Item> savedItems = itemService.createMultipleItems(items, files);
        return ResponseEntity.ok(savedItems);
    }
    
    @RequestMapping(value = "/delete/{itemId}", method = RequestMethod.DELETE)
    public ResponseEntity<Void> deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return ResponseEntity.noContent().build();
    }
    
    @RequestMapping(value = "/edit/{id}", method = RequestMethod.PUT)
    public ResponseEntity<Item> updateItem(@PathVariable Long id, @ModelAttribute Item newItem, @RequestPart("imageFile") MultipartFile imageFile) {
        Item updatedItem = itemService.updateItem(id, newItem,imageFile);
        if (updatedItem != null) {
            return ResponseEntity.ok(updatedItem);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}