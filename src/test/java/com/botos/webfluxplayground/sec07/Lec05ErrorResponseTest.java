package com.botos.webfluxplayground.sec07;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ProblemDetail;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.test.StepVerifier;

class Lec05ErrorResponseTest {

	private static final String BASE_URL = "http://localhost:7070/demo02";

	private static final Logger log = LoggerFactory.getLogger(Lec01MonoClientTest.class);

	@Test
	void handlingError() {
		WebClient.builder()
		         .baseUrl(BASE_URL)
		         .build()
		         .get()
		         .uri("/lec05/calculator/{a}/{b}", 10, 20)
		         .header("operation", "@")
		         .retrieve()
		         .bodyToMono(CalculatorResponse.class)
//		         .onErrorReturn(new CalculatorResponse(0, 0, null, 0.0))
                 .doOnError(WebClientResponseException.class,
                            exception -> log.info("{}", exception.getResponseBodyAs(ProblemDetail.class)))
                 .onErrorReturn(WebClientResponseException.InternalServerError.class,
                                new CalculatorResponse(0, 0, null, 0.0))
                 .onErrorReturn(WebClientResponseException.BadRequest.class,
                                new CalculatorResponse(0, 0, null, -1.0))
                 .doOnNext(response -> log.info(response.toString()))
                 .then()
                 .as(StepVerifier::create)
                 .verifyComplete();
	}
}
