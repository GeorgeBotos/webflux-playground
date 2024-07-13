package com.botos.webfluxplayground.sec10;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

class Lec01HttpConnectionPoolingTest {

//Netstat command to monitor the network connections
//netstat -an| grep -w 127.0.0.1.7070
//
//To watch
//watch 'netstat -an| grep -w 127.0.0.1.7070'

	private static final String BASE_URL = "http://localhost:7070/demo03";

	private static final Logger log = LoggerFactory.getLogger(Lec01HttpConnectionPoolingTest.class);

	@Test
	void concurrentRequest() {
		var max = 10_000;
		Flux.range(1, max)
		    .flatMap(this::getProduct, max)
		    .collectList()
		    .as(StepVerifier::create)
		    .assertNext(products -> assertEquals(max, products.size()))
		    .verifyComplete();
	}

	private Mono<Product> getProduct(int id) {
		var poolSize = 501;
		var provider = ConnectionProvider.builder("george")
		                                 .lifo()
		                                 .maxConnections(poolSize)
		                                 .pendingAcquireMaxCount(5 * poolSize)
		                                 .build();
		var httpClient = HttpClient.create(provider)
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
