package com.botos.webfluxplayground.sec03.controller;

import com.botos.webfluxplayground.sec03.domain.Customer;
import com.botos.webfluxplayground.sec03.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("customers")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping
	public Flux<Customer> allCustomers() {
		return customerService.getAllCustomers();
	}

	@GetMapping("paginated")
	public Mono<Page<Customer>> getCustomer(@RequestParam(defaultValue = "1") Integer page,
	                                        @RequestParam(defaultValue = "3") Integer size) {
		return customerService.getPaginatedAllCustomers(page, size);
	}

	@GetMapping("{id}")
	public Mono<ResponseEntity<Customer>> getCustomer(@PathVariable Integer id) {
		return customerService.getCustomerById(id)
		                      .map(ResponseEntity::ok)
		                      .defaultIfEmpty(ResponseEntity.notFound()
		                                                    .build());
	}

	@PostMapping
	public Mono<ResponseEntity<Customer>> saveCustomer(@RequestBody Mono<Customer> customer) {
		return customerService.saveCustomer(customer)
		                      .map(ResponseEntity::ok);
	}

	@PutMapping("{id}")
	public Mono<ResponseEntity<Customer>> updateCustomer(@PathVariable Integer id,
	                                                     @RequestBody Mono<Customer> customer) {
		return customerService.updateCustomer(id, customer)
		                      .map(ResponseEntity::ok)
		                      .defaultIfEmpty(ResponseEntity.notFound()
		                                                    .build());
	}

	@DeleteMapping("{id}")
	public Mono<ResponseEntity<Void>> deleteCustomer(@PathVariable Integer id) {
		return customerService.deleteCustomerById(id)
		                      .map(success -> success
		                                      ? ResponseEntity.ok()
		                                                      .build()
		                                      : ResponseEntity.notFound()
		                                                      .build());
	}
}
