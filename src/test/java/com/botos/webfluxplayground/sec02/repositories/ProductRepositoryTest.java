package com.botos.webfluxplayground.sec02.repositories;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(properties = {
		"sec=sec02",
		"logging.level.org.springframework.r2dbc=DEBUG"
})
public class ProductRepositoryTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	@DisplayName("Test if findByPriceBetween() returns all the products with a price above the floor but not above the ceiling")
	void testFindByPriceBetween() {
		productRepository.findByPriceBetween(0, 775)
		                 .as(StepVerifier::create)
		                 .expectNextCount(5)
		                 .verifyComplete();
		productRepository.findByPriceBetween(775, 10_000)
		                 .as(StepVerifier::create)
		                 .expectNextCount(5)
		                 .verifyComplete();
		productRepository.findByPriceBetween(350, 450)
		                 .as(StepVerifier::create)
		                 .assertNext(productEntity -> {
			                 assertEquals("apple watch", productEntity.description());
			                 assertEquals(400, productEntity.price());
		                 })
		                 .verifyComplete();

	}

	@Test
	@DisplayName("")
	void testFindByPageable() {
		productRepository.findBy(PageRequest.of(0, 3)
		                                    .withSort(Sort.by("price")
		                                                  .ascending()))
		                 .as(StepVerifier::create)
		                 .assertNext(productEntity -> assertEquals(200, productEntity.price()))
		                 .assertNext(productEntity -> assertEquals(250, productEntity.price()))
		                 .assertNext(productEntity -> assertEquals(300, productEntity.price()))
		                 .verifyComplete();

	}
}
