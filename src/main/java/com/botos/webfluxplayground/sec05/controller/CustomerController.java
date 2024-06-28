package com.botos.webfluxplayground.sec05.controller;

import com.botos.webfluxplayground.sec05.advice.CustomerNotFoundException;
import com.botos.webfluxplayground.sec05.advice.CustomerValidator;
import com.botos.webfluxplayground.sec05.advice.InvalidInputException;
import com.botos.webfluxplayground.sec05.domain.Customer;
import com.botos.webfluxplayground.sec05.filter.Category;
import com.botos.webfluxplayground.sec05.service.CustomerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("customers")
public class CustomerController {

	private final CustomerService customerService;

	@GetMapping
	public Flux<Customer> allCustomers(@RequestAttribute("category") Category category) {
		log.info("category is {}", category);
		return customerService.getAllCustomers();
	}

	@GetMapping("paginated")
	public Mono<Page<Customer>> getCustomer(@RequestParam(defaultValue = "1") Integer page,
	                                        @RequestParam(defaultValue = "3") Integer size) {
		return customerService.getPaginatedAllCustomers(page, size);
	}

	@GetMapping("{id}")
	public Mono<Customer> getCustomer(@PathVariable Integer id) {
		return customerService.getCustomerById(id)
		                      .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)));
	}

	@PostMapping
	public Mono<Customer> saveCustomer(@RequestBody Mono<Customer> customerMono) {
		return customerMono.transform(CustomerValidator.validate())
		                   .as(customerService::saveCustomer);
	}

	@PutMapping("{id}")
	public Mono<Customer> updateCustomer(@PathVariable Integer id,
	                                     @RequestBody Mono<Customer> customerMono) {
		return customerMono.transform(CustomerValidator.validate())
		                   .as(validCustomer -> customerService.updateCustomer(id, validCustomer))
		                   .switchIfEmpty(Mono.error(new InvalidInputException("Valid email is required")));
	}

	@DeleteMapping("{id}")
	public Mono<Void> deleteCustomer(@PathVariable Integer id) {
		return customerService.deleteCustomerById(id)
		                      .filter(saved -> saved)
		                      .switchIfEmpty(Mono.error(new CustomerNotFoundException(id)))
		                      .then();
	}
}
