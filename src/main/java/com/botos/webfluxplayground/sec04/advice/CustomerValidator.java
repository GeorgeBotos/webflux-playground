package com.botos.webfluxplayground.sec04.advice;

import com.botos.webfluxplayground.sec04.domain.Customer;
import reactor.core.publisher.Mono;

import java.util.Objects;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;

public class CustomerValidator {

	public static UnaryOperator<Mono<Customer>> validate() {
		return mono -> mono.filter(hasName())
		                   .switchIfEmpty(Mono.error(new InvalidInputException("Name is required")))
		                   .filter(hasValidEmail())
		                   .switchIfEmpty(Mono.error(new InvalidInputException("Valid email is required")));
	}

	private static Predicate<Customer> hasName() {
		return customer -> Objects.nonNull(customer.name());
	}

	private static Predicate<Customer> hasValidEmail() {
		return customer -> Objects.nonNull(customer.email()) && customer.email()
		                                                                .contains("@");
	}
}
