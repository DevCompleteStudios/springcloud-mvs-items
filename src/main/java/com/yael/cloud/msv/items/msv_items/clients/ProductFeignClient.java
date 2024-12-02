package com.yael.cloud.msv.items.msv_items.clients;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.yael.cloud.msv.items.msv_items.models.Product;



@FeignClient(url="localhost:8001", name="mvs-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Optional<Product> findById(@PathVariable Long id);

}
