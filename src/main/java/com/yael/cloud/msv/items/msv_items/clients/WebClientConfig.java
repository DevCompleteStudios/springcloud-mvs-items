package com.yael.cloud.msv.items.msv_items.clients;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;




@Configuration
public class WebClientConfig {

    @Value("${config.base.uri.mvs}")
    private String uri;
    
    @Bean
    @LoadBalanced
    WebClient.Builder webClient(){
        return WebClient.builder().baseUrl(uri);
    };


}
