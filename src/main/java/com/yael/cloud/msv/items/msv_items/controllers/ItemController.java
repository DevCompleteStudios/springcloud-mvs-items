package com.yael.cloud.msv.items.msv_items.controllers;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.yael.cloud.msv.items.msv_items.models.Item;
import com.yael.cloud.msv.items.msv_items.models.Product;
import com.yael.cloud.msv.items.msv_items.services.ItemService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;




@RefreshScope
@RestController
public class ItemController {

    @Autowired
    private  ItemService service;

    @Autowired
    private CircuitBreakerFactory breakerFactory;
    private final Logger logger = LoggerFactory.getLogger(ItemController.class);

    @Value("${configuracion.texto}")
    private String text;

    @Autowired
    Environment envs;


    @GetMapping("/get/config")
    public ResponseEntity<?> fetch( @Value("${server.port}") String port ){
        Map<String, Object> json = new HashMap<>();

        json.put("text", text);
        json.put("port", port);

        logger.info(port);
        logger.info(text);

        if(envs.getActiveProfiles().length > 0 && envs.getActiveProfiles()[0].equals("dev")){
            json.put("autor", envs.getProperty("configuracion.autor.nombre"));
            json.put("email", envs.getProperty("configuracion.autor.email"));
        }

        return ResponseEntity.ok(json);
    }


    @GetMapping
    public ResponseEntity<?> findAll( @RequestParam(name="name", required=false) String name,
        @RequestHeader(name="token-request", required=false) String token )
    {
        System.out.println("Name: " + name);
        System.out.println("Token: " + token);
        return ResponseEntity.ok(service.findAll());
    }


    @CircuitBreaker(name="items", fallbackMethod="getFallbackMethodProduct") // nombre de la configuración del corto circuito (application.yml)
    @GetMapping("/details/{id}")
    public ResponseEntity<?> detailsfindById(@PathVariable long id) {
        Optional<Item> item = service.findById(id); // usamos el breaker

        if(item.isPresent()){
            return ResponseEntity.ok(item.get());
        }
        return ResponseEntity.status(404).body(Collections.singletonMap("message", "Not found"));
    }

    @TimeLimiter(name="items") // si no tiene el decorador para el corto circuito nunca entrara
    @CircuitBreaker(name="items", fallbackMethod="getFallbackMethodProduct")
    @GetMapping("/details2/{id}")
    public CompletableFuture<?> details3findById(@PathVariable long id) {
        return CompletableFuture.supplyAsync(() -> {
            Optional<Item> item = service.findById(id); // usamos el breaker

            if(item.isPresent()){
                return ResponseEntity.ok(item.get());
            }
            return ResponseEntity.status(404).body(Collections.singletonMap("message", "Not found"));
        });
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


    public ResponseEntity<?> getFallbackMethodProduct(Throwable e){
        logger.info(e.getMessage());

        Item items = new Item(new Product(1L, "", 100.2, LocalDateTime.now()), 5);
        return ResponseEntity.of(Optional.of(items));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Product create(@RequestBody Product product){
        return service.save(product);
    }

    @PutMapping("/{id}")
    public Product update( @PathVariable Long id, @RequestBody Product product ){
        return service.update(product, id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete( @PathVariable Long id ){
        service.delete(id);
    }

}
