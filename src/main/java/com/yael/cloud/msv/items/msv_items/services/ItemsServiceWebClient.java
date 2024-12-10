package com.yael.cloud.msv.items.msv_items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.libs.msv.commons.entities.Product;



public class ItemsServiceWebClient implements ItemService {

    @Autowired
    private WebClient.Builder client;


    @Override
    public List<Item> findAll() {
        return client.build()
            .get()
            .accept(MediaType.APPLICATION_JSON)
            .retrieve()
            .bodyToFlux( Product.class )
            .map(p -> new Item(p, new Random().nextInt(1,9)))
            .collectList()
            .block(); // esperamos a que se resuelva
    }

    @Override
    public Optional<Item> findById(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        // try {
            return Optional.of(
                client.build().get().uri("/{id}", params)
                .accept()
                .retrieve()
                .bodyToMono(Product.class)
                .map(p -> new Item(p, new Random().nextInt(1,9)))
                .block()
            );
        // } catch (WebClientResponseException e) {
        //     return Optional.empty();
        // }
    }

    @Override
    public Product save(Product product) {
        return client.build()
            .post()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(product)
            .retrieve()
            .bodyToMono( Product.class )
            .block();
    }

    @Override
    public Product update(Product product, Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        return client.build()
            .put()
            .uri("/{id}", params)
            .accept()
            .contentType(MediaType.APPLICATION_JSON)
            .bodyValue(product)
            .retrieve()
            .bodyToMono(Product.class)
            .block();
    }

    @Override
    public void delete(Long id) {
        Map<String, Object> params = new HashMap<>();
        params.put("id", id);

        client.build()
            .delete()
            .uri("/{id}", params)
            .retrieve()
            .bodyToMono(Void.class)
            .block();
    }

}
