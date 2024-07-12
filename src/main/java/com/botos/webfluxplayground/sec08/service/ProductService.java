package com.botos.webfluxplayground.sec08.service;

import com.botos.webfluxplayground.sec08.domain.Product;
import com.botos.webfluxplayground.sec08.domain.ProductEntity;
import com.botos.webfluxplayground.sec08.domain.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class ProductService {

	private final ProductRepository productRepository;

	public Flux<Product> saveProducts(Flux<Product> productFlux) {
		return productFlux.map(ProductEntity::fromProduct)
		                  .as(productRepository::saveAll)
		                  .map(Product::fromEntity);
	}

	public Mono<Long> getProductsCount() {
		return productRepository.count();
	}

	public Flux<Product> getAllProducts() {
		return productRepository.findAll()
		                        .map(Product::fromEntity);
	}
}
