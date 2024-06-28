package com.botos.webfluxplayground.sec05;

import com.botos.webfluxplayground.sec05.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec05")
public class CustomerServiceTest {

	@Autowired
	private WebTestClient client;

	@Test
	@DisplayName("when we have no token set in header, unauthorized should be returned")
	void testNoToken() {
		client.get()
		      .uri("/customers")
		      .exchange()
		      .expectStatus().isEqualTo(HttpStatus.UNAUTHORIZED);
	}

	@Test
	@DisplayName("when we have incorrect token set in header, unauthorized should be returned")
	void testIncorrectToken() {
		validateGet("secret", HttpStatus.UNAUTHORIZED);
	}

	@Test
	@DisplayName("when we have standard token set in header, get request should be successful")
	void testStandardTokenGet() {
		validateGet("secret123", HttpStatus.OK);
	}

	@Test
	@DisplayName("when we have standard token set in header, post request should be forbidden")
	void testStandardTokenPost() {
		validatePost("secret123", HttpStatus.FORBIDDEN);
	}

	@Test
	@DisplayName("when we have prime token set in header, get request should be successful")
	void testPrimeTokenGet() {
		validateGet("secret456", HttpStatus.OK);
	}

	@Test
	@DisplayName("when we have prime token set in header, post request should be successful")
	void testPrimeTokenPost() {
		validatePost("secret456", HttpStatus.OK);
	}


	private void validateGet(String token, HttpStatus expectedStatus) {
		client.get()
		      .uri("/customers")
		      .header("auth-token", token)
		      .exchange()
		      .expectStatus().isEqualTo(expectedStatus);
	}

	private void validatePost(String token, HttpStatus expectedStatus) {
		var customer = Customer.builder()
		                       .name("mashal")
		                       .email("marshal@gmail.com")
		                       .build();
		client.post()
		      .uri("/customers")
		      .bodyValue(customer)
		      .header("auth-token", token)
		      .exchange()
		      .expectStatus().isEqualTo(expectedStatus);
	}
}
