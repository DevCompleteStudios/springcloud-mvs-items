package com.yael.cloud.msv.items.msv_items.services;

import java.util.List;
import java.util.Optional;

import com.yael.cloud.msv.items.msv_items.models.Item;



public interface ItemService {
    List<Item> findAll();
    Optional<Item> findById(Long id);
}

