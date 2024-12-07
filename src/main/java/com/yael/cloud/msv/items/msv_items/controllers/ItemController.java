package com.yael.cloud.msv.items.msv_items.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.cloud.msv.items.msv_items.models.Product;
import com.yael.cloud.msv.items.msv_items.services.ItemService;





@RestController
public class ItemController {

    @Autowired
    private  ItemService service;

    @Autowired
    private CircuitBreakerFactory breakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);


    @GetMapping
    public ResponseEntity<?> findAll( @RequestParam(name="name", required=false) String name,
        @RequestHeader(name="token-request", required=false) String token )
    {
        System.out.println("Name: " + name);
        System.out.println("Token: " + token);
        return ResponseEntity.ok(service.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable long id) {
        Optional<Item> item = breakerFactory.create("items")
            .run(() -> service.findById(id), e -> {
                logger.info(e.getMessage());

                Item items = new Item(new Product(1L, "", 100.2, LocalDateTime.now()), 5);
                return Optional.of(items);
            }); // usamos el breaker

        if(item.isPresent()){
            return ResponseEntity.ok(item.get());
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("message", "Not found"));
    }

}
