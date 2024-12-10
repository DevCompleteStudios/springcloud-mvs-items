package com.yael.cloud.msv.items.msv_items.services;

import java.util.List;
import java.util.Optional;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.libs.msv.commons.entities.Product;



public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
    Product save(Product product);
    Product update(Product product, Long id);
    void delete(Long id);
}

