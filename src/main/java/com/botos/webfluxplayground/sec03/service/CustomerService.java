package com.botos.webfluxplayground.sec03.service;

import com.botos.webfluxplayground.sec03.domain.Customer;
import com.botos.webfluxplayground.sec03.domain.CustomerEntity;
import com.botos.webfluxplayground.sec03.domain.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class CustomerService {

	private final CustomerRepository customerRepository;

	public Flux<Customer> getAllCustomers() {
		return customerRepository.findAll()
		                         .map(Customer::fromEntity);
	}

	public Mono<Page<Customer>> getPaginatedAllCustomers(final Integer page, final Integer size) {
		var pageRequest = PageRequest.of(page - 1, size);
		return customerRepository.findBy(pageRequest)
		                         .map(Customer::fromEntity)
		                         .collectList()
		                         .zipWith(customerRepository.count())
		                         .map(tuple -> new PageImpl<>(tuple.getT1(), pageRequest, tuple.getT2()));
	}

	public Mono<Customer> getCustomerById(final Integer id) {
		return customerRepository.findById(id)
		                         .map(Customer::fromEntity);
	}

	public Mono<Customer> saveCustomer(final Mono<Customer> customerMono) {
		return customerMono.map(CustomerEntity::fromCustomer)
		                   .flatMap(customerRepository::save)
		                   .map(Customer::fromEntity);
	}

	public Mono<Customer> updateCustomer(final Integer id, final Mono<Customer> customerMono) {
		return customerRepository.findById(id)
		                         .flatMap(entity -> customerMono)
		                         .map(customer -> CustomerEntity.builder()
		                                                        .id(id)
		                                                        .name(customer.name())
		                                                        .email(customer.email())
		                                                        .build())
		                         .flatMap(customerRepository::save)
		                         .map(Customer::fromEntity);
	}

	public Mono<Boolean> deleteCustomerById(final Integer id) {
		return customerRepository.deleteCustomerById(id);
	}

	public Mono<Long> getCount() {
		return customerRepository.count();
	}
}
