package com.botos.webfluxplayground.sec02.repositories;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(properties = {
		"sec=sec02",
		"logging.level.org.springframework.r2dbc=DEBUG"
})
public class Lec01CustomerRepositoryTest {

	private static final Logger log = LoggerFactory.getLogger(Lec01CustomerRepositoryTest.class);

	@Autowired
	private CustomerRepository customerRepository;

	@Test
	void findAll() {
		customerRepository.findAll()
		                  .doOnNext(customer -> log.info("{}", customer))
		                  .as(StepVerifier::create)
		                  .expectNextCount(10)
		                  .verifyComplete();
	}

	@Test
	void findById() {
		customerRepository.findById(2)
		                  .doOnNext(customer -> log.info("{}", customer))
		                  .as(StepVerifier::create)
		                  .assertNext(customer -> assertEquals("mike", customer.getName()))
		                  .verifyComplete();
	}

	@Test
	void findByName() {
		customerRepository.findByName("jake")
		                  .doOnNext(customer -> log.info("{}", customer))
		                  .as(StepVerifier::create)
		                  .assertNext(customer -> assertEquals("jake@gmail.com", customer.getEmail()))
		                  .verifyComplete();
	}

	@Test
	void findByEmailEndingWith() {
		customerRepository.findByEmailEndingWith("ke@gmail.com")
		                  .doOnNext(customer -> log.info("{}", customer))
		                  .as(StepVerifier::create)
		                  .assertNext(customer -> assertEquals("mike@gmail.com", customer.getEmail()))
		                  .assertNext(customer -> assertEquals("jake@gmail.com", customer.getEmail()))
		                  .verifyComplete();
	}

	@Test
	void insertAndDeleteCustomer() {
		var customer = CustomerEntity.builder()
		                             .name("marshal")
		                             .email("marshal@gmail.com")
		                             .build();
		customerRepository.count()
		                  .as(StepVerifier::create)
		                  .expectNext(10L)
		                  .verifyComplete();
		customerRepository.save(customer)
		                  .as(StepVerifier::create)
		                  .assertNext(customerEntity -> assertNotNull(customerEntity.getId()))
		                  .verifyComplete();
		customerRepository.count()
		                  .as(StepVerifier::create)
		                  .expectNext(11L)
		                  .verifyComplete();
		customerRepository.deleteById(11)
		                  .then(customerRepository.count())
		                  .as(StepVerifier::create)
		                  .expectNext(10L)
		                  .verifyComplete();
	}

	@Test
	void updateCustomer() {
		customerRepository.findByName("ethan")
		                  .doOnNext(customerEntity -> customerEntity.setName("noel"))
		                  .flatMap(customerEntity -> customerRepository.save(customerEntity))
		                  .as(StepVerifier::create)
		                  .assertNext(customerEntity -> assertEquals("noel", customerEntity.getName()))
		                  .verifyComplete();
	}
}
