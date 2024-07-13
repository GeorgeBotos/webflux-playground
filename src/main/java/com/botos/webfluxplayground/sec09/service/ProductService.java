package com.botos.webfluxplayground.sec09.service;

import com.botos.webfluxplayground.sec09.domain.Product;
import com.botos.webfluxplayground.sec09.domain.ProductEntity;
import com.botos.webfluxplayground.sec09.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Sinks;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	private final Sinks.Many<Product> sink;

	public Mono<Product> saveProduct(Mono<Product> productMono) {
		return productMono.map(ProductEntity::fromProduct)
		                  .flatMap(productRepository::save)
		                  .map(Product::fromEntity)
		                  .doOnNext(sink::tryEmitNext);
	}

	public Flux<Product> productStream() {
		return sink.asFlux();
	}
}
