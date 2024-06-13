package com.botos.webfluxplayground.sec01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("traditional")
public class TraditionalWebController {

	private final RestClient restClient = RestClient.builder()
	                                                .build();

	@GetMapping("products")
	public List<Product> getProducts() {
		var products = restClient.get()
		                         .uri("http://localhost:7070/demo01/products")
		                         .retrieve()
		                         .body(new ParameterizedTypeReference<List<Product>>() {});
		log.info("received response: {}", products);
		return products;
	}

	@GetMapping("products2")
	public List<Product> getProducts2() {
		var products = restClient.get()
		                         .uri("http://localhost:7070/demo01/products/notorious")
		                         .retrieve()
		                         .body(new ParameterizedTypeReference<List<Product>>() {});
		log.info("received response: {}", products);
		return products;
	}
}
