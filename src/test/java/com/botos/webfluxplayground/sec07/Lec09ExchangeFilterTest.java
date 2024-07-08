package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.UUID;

public class Lec09ExchangeFilterTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void basicAuth() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .filter(generateToken())
		         .filter(requestLogger())
		         .build()
		         .get()
		         .uri("/lec09/product/{id}", 1)
		         .retrieve()
		         .bodyToMono(Product.class)
		         .doOnNext(product -> log.info(product.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	private ExchangeFilterFunction generateToken() {
		return (request, next) -> {
			var token = UUID.randomUUID()
			                .toString()
			                .replace("-", "");
			log.info("generated token: {}", token);
			var requestWithAuth = ClientRequest.from(request)
			                                   .headers(httpHeaders -> httpHeaders.setBearerAuth(token))
			                                   .build();
			return next.exchange(requestWithAuth);
		};
	}

	private ExchangeFilterFunction requestLogger() {
		return (request, next) -> {
			log.info("request url - {}: {}", request.method(), request.url());
			return next.exchange(request);
		};
	}
}
