package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;

class Lec01MonoClientTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void simpleGet() throws InterruptedException {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri("/lec01/product/1")
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .subscribe();

		Thread.sleep(Duration.ofSeconds(2));
	}

	@Test
	void concurrentRequest() throws InterruptedException {
		for (int i = 0; i <= 100; i++) {
			WebClient.builder()
			         .baseUrl(BASE_URL)
			         .build()
			         .get()
			         .uri("/lec01/product/{id}", i)
			         .retrieve()
			         .bodyToMono(Product.class)
			         .doOnNext(product -> log.info(product.toString()))
			         .subscribe();
		}

		Thread.sleep(Duration.ofSeconds(2));
	}
}
