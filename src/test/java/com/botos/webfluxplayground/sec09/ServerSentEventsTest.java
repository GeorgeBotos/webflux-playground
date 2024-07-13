package com.botos.webfluxplayground.sec09;

import com.botos.webfluxplayground.sec09.domain.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec09")
public class ServerSentEventsTest {

	private static final Logger log = LoggerFactory.getLogger(ServerSentEventsTest.class);

	@Autowired
	private WebTestClient webTestClient;

	@Test
	void testServerEvents() {
		webTestClient.get()
		             .uri("/products/stream/{maxPrice}")
		             .accept(MediaType.TEXT_EVENT_STREAM)
		             .exchange()
		             .expectStatus().is2xxSuccessful()
		             .returnResult(Product.class)
		             .getResponseBody()
		             .take(3)
		             .doOnNext(product -> log.info("received: {}", product))
		             .collectList()
		             .as(StepVerifier::create)
		             .assertNext(products -> {
			             assertEquals(3, products.size());
			             assertTrue(products.stream()
			                                .allMatch(product -> product.price() <= 80));
		             })
		             .verifyComplete();
	}
}
