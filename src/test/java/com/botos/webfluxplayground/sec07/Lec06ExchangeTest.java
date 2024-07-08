package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

class Lec06ExchangeTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void exchange() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri("/lec05/calculator/{a}/{b}", 10, 20)
		         .header("operation", "@")
		         .exchangeToMono(this::decode)


		         .doOnNext(response -> log.info(response.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	private Mono<CalculatorResponse> decode(ClientResponse clientResponse) {
		log.info("status code: {}", clientResponse.statusCode());
		if (clientResponse.statusCode()
		                  .isError()) {
			return clientResponse.bodyToMono(ProblemDetail.class)
			                     .doOnNext(problemDetail -> log.info("{}", problemDetail))
			                     .then(Mono.empty());
		}
		return clientResponse.bodyToMono(CalculatorResponse.class);
	}
}
