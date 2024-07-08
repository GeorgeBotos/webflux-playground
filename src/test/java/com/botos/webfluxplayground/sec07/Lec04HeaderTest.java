package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

class Lec04HeaderTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void testDefaultHeader() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .defaultHeader("caller-id", "order-service")
		         .build()
		         .get()
		         .uri("/lec04/product/{id}", 1)
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	@Test
	void testHeadersWithMap() {
		var headerValues = Map.ofEntries(Map.entry("caller-id", "order-service"),
		                                 Map.entry("some-key", "some-value"));
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri("/lec04/product/{id}", 1)
		         .headers(headers -> headers.setAll(headerValues))
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}
}