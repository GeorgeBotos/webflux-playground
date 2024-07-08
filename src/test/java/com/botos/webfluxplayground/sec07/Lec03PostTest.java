package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec03PostTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void testPostRequest() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .post()
		         .uri("/lec03/product")
		         .bodyValue(Product.builder()
		                           .description("iphone")
		                           .price(1000)
		                           .build())
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();

	}

	@Test
	void testPostWithMonoRequest() {
		var productMono = Mono.fromSupplier(() -> Product.builder()
		                                                 .description("iphone")
		                                                 .price(1000)
		                                                 .build());
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .post()
		         .uri("/lec03/product")
		         .body(productMono, Product.class)
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();

	}
}
