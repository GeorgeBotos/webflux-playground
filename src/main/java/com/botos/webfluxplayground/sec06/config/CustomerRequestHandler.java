package com.botos.webfluxplayground.sec06.config;

import com.botos.webfluxplayground.sec06.advice.CustomerNotFoundException;
import com.botos.webfluxplayground.sec06.advice.CustomerValidator;
import com.botos.webfluxplayground.sec06.domain.Customer;
import com.botos.webfluxplayground.sec06.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerRequestHandler {

	private final CustomerService customerService;

	public Mono<ServerResponse> getAllCustomers(ServerRequest ignored) {
		return customerService.getAllCustomers()
		                      .as(customerFlux -> ServerResponse.ok()
		                                                        .body(customerFlux, Customer.class));
	}

	public Mono<ServerResponse> getCustomer(ServerRequest request) {
		var customerId = Integer.parseInt(request.pathVariable("id"));
		return customerService.getCustomerById(customerId)
		                      .switchIfEmpty(Mono.error(new CustomerNotFoundException(customerId)))
		                      .flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> saveCustomer(ServerRequest request) {
		return request.bodyToMono(Customer.class)
		              .transform(CustomerValidator.validate())
		              .as(customerService::saveCustomer)
		              .flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> updateCustomer(ServerRequest request) {
		var customerId = Integer.parseInt(request.pathVariable("id"));
		return request.bodyToMono(Customer.class)
		              .transform(CustomerValidator.validate())
		              .as(customerMono -> customerService.updateCustomer(customerId, customerMono))
		              .switchIfEmpty(Mono.error(new CustomerNotFoundException(customerId)))
		              .flatMap(ServerResponse.ok()::bodyValue);
	}

	public Mono<ServerResponse> deleteCustomer(ServerRequest request) {
		var customerId = Integer.parseInt(request.pathVariable("id"));
		return customerService.deleteCustomerById(customerId)
		                      .filter(booleanResponse -> booleanResponse)
		                      .switchIfEmpty(Mono.error(new CustomerNotFoundException(customerId)))
		                      .then(ServerResponse.ok()
		                                          .build());

	}

	public Mono<ServerResponse> getPaginatedCustomers(ServerRequest request) {
		var page = request.queryParam("page")
		                  .map(Integer::parseInt)
		                  .orElse(1);
		var size = request.queryParam("size")
		                  .map(Integer::parseInt)
		                  .orElse(3);
		return customerService.getPaginatedAllCustomers(page, size)
		                      .flatMap(ServerResponse.ok()::bodyValue);
	}
}
