package com.botos.webfluxplayground.sec06;

import com.botos.webfluxplayground.sec03.domain.Customer;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

@AutoConfigureWebTestClient
@SpringBootTest(properties = "sec=sec06")
class CustomerControllerTest {

	@Autowired
	private WebTestClient client;

	@Test
	@DisplayName("test if getAllCustomers() returns all the customers")
	void allCustomers() {
		client.get()
		      .uri("/customers")
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectHeader()
		      .contentType(MediaType.APPLICATION_JSON)
		      .expectBodyList(Customer.class)
		      .hasSize(10);
	}

	@Test
	@DisplayName("test if getCustomers() returns the requested number of customers")
	void paginatedCustomers() {
		client.get()
		      .uri("/customers/paginated?page=3&size=2")
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectHeader()
		      .contentType(MediaType.APPLICATION_JSON)
		      .expectBody()
		      .jsonPath("$.content.length()")
		      .isEqualTo(2)
		      .jsonPath("$.content[0].id")
		      .isEqualTo(5)
		      .jsonPath("$.content[1].id")
		      .isEqualTo(6);
	}

	@Test
	@DisplayName("test if getCustomerById() returns the correct customer")
	void customerById() {
		client.get()
		      .uri("/customers/1")
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectHeader()
		      .contentType(MediaType.APPLICATION_JSON)
		      .expectBody()
		      .jsonPath("$.name")
		      .isEqualTo("sam");
	}

	@Test
	@DisplayName("test creating and deleting a customer")
	void createAndDelete() {
		client.post()
		      .uri("/customers")
		      .bodyValue(Customer.builder()
		                         .name("marshal")
		                         .email("marshal@gmail.com")
		                         .build())
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectBody()
		      .jsonPath("$.id")
		      .isEqualTo(11)
		      .jsonPath("$.name")
		      .isEqualTo("marshal")
		      .jsonPath("$.email")
		      .isEqualTo("marshal@gmail.com");
		client.delete()
		      .uri("/customers/11")
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectBody()
		      .isEmpty();
	}

	@Test
	@DisplayName("test updating a customer")
	void update() {
		client.put()
		      .uri("/customers/10")
		      .bodyValue(Customer.builder()
		                         .name("marshal")
		                         .email("marshal@gmail.com")
		                         .build())
		      .exchange()
		      .expectStatus()
		      .is2xxSuccessful()
		      .expectBody()
		      .jsonPath("$.id")
		      .isEqualTo(10)
		      .jsonPath("$.name")
		      .isEqualTo("marshal")
		      .jsonPath("$.email")
		      .isEqualTo("marshal@gmail.com");
	}

	@Test
	@DisplayName("customer not found test")
	void customerNotFound() {
		client.get()
		      .uri("/customers/11")
		      .exchange()
		      .expectStatus()
		      .is4xxClientError()
		      .expectBody()
		      .jsonPath("$.detail")
		      .isEqualTo("Customer [id = 11] not found");
	}

	@Test
	@DisplayName("test for invalid input")
	void invalidInput() {
		var customerMissingName = new Customer(null, null, "email@mail.com");
		var customerMissingEmail = new Customer(null, "name", null);
		var customerInvalidEmail = new Customer(null, "name", "mail.com");

		client.post()
		      .uri("/customers")
		      .bodyValue(customerMissingName)
		      .exchange()
		      .expectStatus()
		      .is4xxClientError()
		      .expectBody()
		      .jsonPath("$.detail")
		      .isEqualTo("Name is required");

		client.post()
		      .uri("/customers")
		      .bodyValue(customerMissingEmail)
		      .exchange()
		      .expectStatus()
		      .is4xxClientError()
		      .expectBody()
		      .jsonPath("$.detail")
		      .isEqualTo("Valid email is required");

		client.put()
		      .uri("/customers/11")
		      .bodyValue(customerInvalidEmail)
		      .exchange()
		      .expectStatus()
		      .is4xxClientError()
		      .expectBody()
		      .jsonPath("$.detail")
		      .isEqualTo("Valid email is required");
	}
}
