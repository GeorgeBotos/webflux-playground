package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

class Lec08BearerAuthTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void basicAuth() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .defaultHeaders(httpHeaders -> httpHeaders.setBearerAuth("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9"))
		         .build()
		         .get()
		         .uri("/lec08/product/{id}", 1)
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}
}
