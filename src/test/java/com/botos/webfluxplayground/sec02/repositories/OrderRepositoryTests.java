package com.botos.webfluxplayground.sec02.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(properties = {
		"sec=sec02",
		"logging.level.org.springframework.r2dbc=DEBUG"
})
public class OrderRepositoryTests {

	@Autowired
	private OrderRepository orderRepository;

	@Test
	@DisplayName("Test if getProductsOrderedByCustomer() returns all products ordered by customer")
	void productsOrderedByCustomer() {
		orderRepository.getProductsOrderedByCustomer("mike")
		               .as(StepVerifier::create)
		               .expectNextCount(2)
		               .verifyComplete();
	}

	@Test
	@DisplayName("Test if getOrderDetailsByProduct() returns the correct orderLines")
	void orderDetailsByProduct() {
		orderRepository.getOrderDetailsByProduct("iphone 20")
		               .as(StepVerifier::create)
		               .assertNext(orderLine -> assertEquals(975, orderLine.amount()))
		               .assertNext(orderLine -> assertEquals(950, orderLine.amount()))
		               .verifyComplete();

	}
}
