package com.yael.cloud.msv.items.msv_items.services;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yael.cloud.msv.items.msv_items.clients.ProductFeignClient;
import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.libs.msv.commons.entities.Product;

import feign.FeignException;



@Service
public class ItemServiceFeign implements ItemService {

    @Autowired
    private ProductFeignClient client;

    private final Random RANDOM = new Random();


    @Override
    public List<Item> findAll() {
        return client.findAll().stream()
            .map( p -> new Item(p, RANDOM.nextInt(1, 10)))
            .toList();
    }

    @Override
    public Optional<Item> findById(Long id) {
        try {
            Optional<Product> product = client.findById(id);
            return Optional.of(new Item(product.get(), RANDOM.nextInt(1,9)));
        } catch(FeignException ex) {
            return Optional.empty();
        }
    }

    @Override
    public Product save(Product product) {
        return client.create(product);
    }

    @Override
    public Product update(Product product, Long id) {
        return client.update(product, id);
    }

    @Override
    public void delete(Long id) {
        client.delete(id);
    }

}
