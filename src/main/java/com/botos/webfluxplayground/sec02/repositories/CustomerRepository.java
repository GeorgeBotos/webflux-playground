package com.botos.webfluxplayground.sec02.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface CustomerRepository extends ReactiveCrudRepository<CustomerEntity, Integer> {

	Flux<CustomerEntity> findByName(String name);

	Flux<CustomerEntity> findByEmailEndingWith(String email);
}
