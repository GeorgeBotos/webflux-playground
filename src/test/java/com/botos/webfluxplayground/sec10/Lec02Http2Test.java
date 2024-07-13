package com.botos.webfluxplayground.sec10;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.HttpProtocol;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Lec02Http2Test {

	private static final String BASE_URL = "http://localhost:7070/demo03";

	private static final Logger log = LoggerFactory.getLogger(Lec02Http2Test.class);

	@Test
	void concurrentRequest() {
		var max = 20_000;
		Flux.range(1, max)
		    .flatMap(this::getProduct, max)
		    .collectList()
		    .as(StepVerifier::create)
		    .assertNext(products -> assertEquals(max, products.size()))
		    .verifyComplete();
	}

	private Mono<Product> getProduct(int id) {
		var poolSize = 1;
		var provider = ConnectionProvider.builder("george")
		                                 .lifo()
		                                 .maxConnections(poolSize)
		                                 .pendingAcquireMaxCount(5 * poolSize)
		                                 .build();
		var httpClient = HttpClient.create(provider)
		                           .protocol(HttpProtocol.H2C)
		                           .compress(true)
		                           .keepAlive(true);
		return WebClient.builder()
		                .baseUrl(BASE_URL)
		                .clientConnector(new ReactorClientHttpConnector(httpClient))
		                .build()
		                .get()
		                .uri("/product/{id}", id)
		                .retrieve()
		                .bodyToMono(Product.class);
	}
}
