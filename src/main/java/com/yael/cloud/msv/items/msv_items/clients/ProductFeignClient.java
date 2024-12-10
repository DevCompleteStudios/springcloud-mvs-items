package com.yael.cloud.msv.items.msv_items.clients;

import java.util.List;
import java.util.Optional;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.yael.libs.msv.commons.entities.Product;




@FeignClient(name="mvs-products")
public interface ProductFeignClient {

    @GetMapping
    List<Product> findAll();

    @GetMapping("/{id}")
    Optional<Product> findById(@PathVariable Long id);

    @PostMapping
    public Product create( @RequestBody Product product );

    @PutMapping("/{id}")
    public Product update( @RequestBody Product product, @PathVariable Long id );

    @DeleteMapping("/{id}")
    public void delete( @PathVariable Long id );

}
