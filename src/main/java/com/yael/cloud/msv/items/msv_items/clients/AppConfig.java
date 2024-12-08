package com.yael.cloud.msv.items.msv_items.clients;

import java.time.Duration;

import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;



@Configuration
public class AppConfig {

    @Bean
    Customizer<Resilience4JCircuitBreakerFactory> customizerCircuitBreaker(){ // configuración para la tolerancia a fallos
        return (factory) -> factory.configureDefault(id -> {
            return new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.custom()
                    .slidingWindowSize(10)
                    .failureRateThreshold(50)
                    .waitDurationInOpenState(Duration.ofSeconds(6))
                    .permittedNumberOfCallsInHalfOpenState(5)
                    .slowCallDurationThreshold(Duration.ofSeconds(2)) // maximo de duración de una llamada lenta
                    .slowCallRateThreshold(50) // porcentaje de llamadas lentas
                    .build()
                )
                .timeLimiterConfig(
                    TimeLimiterConfig.custom()
                        .timeoutDuration(Duration.ofSeconds(2)) // entra en el lado alternativo (entra primero que la llamada lenta)
                        .build()
                )
                .build();
        });
    };

}
