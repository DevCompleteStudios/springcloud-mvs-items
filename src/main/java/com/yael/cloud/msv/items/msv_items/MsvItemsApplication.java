package com.yael.cloud.msv.items.msv_items;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;


@EnableFeignClients
@SpringBootApplication
public class MsvItemsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsvItemsApplication.class, args);
	}

}
