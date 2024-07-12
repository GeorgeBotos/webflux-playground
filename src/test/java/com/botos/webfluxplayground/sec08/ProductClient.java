package com.botos.webfluxplayground.sec08;

import com.botos.webfluxplayground.sec08.domain.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class ProductClient {

	private final static Logger log = LoggerFactory.getLogger(ProductClient.class);

	private final WebClient client = WebClient.builder()
	                                          .baseUrl("http://localhost:8080")
	                                          .build();

	public Mono<UploadResponse> uploadProducts(Flux<Product> productFlux) {
		return client.post()
		             .uri("/products/upload")
		             .contentType(MediaType.APPLICATION_NDJSON)
		             .body(productFlux, Product.class)
		             .retrieve()
		             .bodyToMono(UploadResponse.class);
	}

	public Flux<Product> downloadProducts() {
		return client.get()
		             .uri("/products/download")
		             .accept(MediaType.APPLICATION_NDJSON)
		             .retrieve()
		             .bodyToFlux(Product.class);
	}
}
