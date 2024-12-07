package com.yael.cloud.msv.items.msv_items.services;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.cloud.msv.items.msv_items.models.Product;




@Service
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

}
