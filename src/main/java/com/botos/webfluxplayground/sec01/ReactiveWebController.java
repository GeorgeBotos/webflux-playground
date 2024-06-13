package com.botos.webfluxplayground.sec01;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Slf4j
@RestController
@RequestMapping("reactive")
public class ReactiveWebController {

	private final WebClient webClient = WebClient.builder()
	                                             .baseUrl("http://localhost:7070")
	                                             .build();

	@GetMapping("products")
	public Flux<Product> getProducts() {
		return webClient.get()
		                .uri("/demo01/products/notorious")
		                .retrieve()
		                .bodyToFlux(Product.class)
		                .onErrorComplete()
		                .doOnNext(product -> log.info("received: {}", product));
	}

	@GetMapping(value = "products/stream",
	            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Product> getProductsStream() {
		return webClient.get()
		                .uri("/demo01/products")
		                .retrieve()
		                .bodyToFlux(Product.class)
		                .doOnNext(product -> log.info("received: {}", product));
	}
}
