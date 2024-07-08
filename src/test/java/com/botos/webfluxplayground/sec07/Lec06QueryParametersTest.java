package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.test.StepVerifier;

import java.util.Map;

class Lec06QueryParametersTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void uriBuilderWithVariables() {
		var path = "/lec06/calculator";
		var query = "first={first}&second={second}&operation={operation}";
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri(uriBuilder -> uriBuilder.path(path)
		                                      .query(query)
		                                      .build(10, 20, "+"))
		         .retrieve()
		         .bodyToMono(CalculatorResponse.class)
		         .doOnNext(calculatorResponse -> log.info(calculatorResponse.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}

	@Test
	void uriBuilderWithMap() {
		var path = "/lec06/calculator";
		var query = "first={first}&second={second}&operation={operation}";
		var variables = Map.ofEntries(Map.entry("first", 10),
		                              Map.entry("second", 20),
		                              Map.entry("operation", "+"));
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri(uriBuilder -> uriBuilder.path(path)
		                                      .query(query)
		                                      .build(variables))
		         .retrieve()
		         .bodyToMono(CalculatorResponse.class)
		         .doOnNext(calculatorResponse -> log.info(calculatorResponse.toString()))
		         .then()
		         .as(StepVerifier::create)
		         .verifyComplete();
	}
}
