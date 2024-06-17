package com.botos.webfluxplayground.sec02.repositories;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface ProductRepository extends CrudRepository<ProductEntity, Integer> {

	Flux<ProductEntity> findByPriceBetween(Integer floor, Integer ceiling);

	Flux<ProductEntity> findBy(Pageable pageable);
}
