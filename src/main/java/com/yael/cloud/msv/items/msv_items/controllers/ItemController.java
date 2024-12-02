package com.yael.cloud.msv.items.msv_items.controllers;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.cloud.msv.items.msv_items.services.ItemService;





@RestController
public class ItemController {

    @Autowired
    private  ItemService service;


    @GetMapping
    ResponseEntity<?> findAll(){
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Optional<Item> item = service.findById(id);

        if(item.isPresent()){
            return ResponseEntity.ok(item.get());
        }
        return ResponseEntity.notFound().build();
    }
    

}
