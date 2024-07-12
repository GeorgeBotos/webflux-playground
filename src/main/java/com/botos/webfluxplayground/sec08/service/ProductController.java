package com.botos.webfluxplayground.sec08.service;

import com.botos.webfluxplayground.sec08.UploadResponse;
import com.botos.webfluxplayground.sec08.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

	private final ProductService productService;

	@PostMapping(value = "upload",
	             consumes = MediaType.APPLICATION_NDJSON_VALUE)
	public Mono<UploadResponse> uploadProducts(@RequestBody Flux<Product> productFlux) {
		log.info("invoked");
		return productService.saveProducts(productFlux.doOnNext(product -> log.info("received: {}", product)))
		                     .then(productService.getProductsCount())
		                     .map(UploadResponse::new);
	}

	@GetMapping(value = "download",
	            produces = MediaType.APPLICATION_NDJSON_VALUE)
	public Flux<Product> download() {
		return productService.getAllProducts()
		                     .delayElements(Duration.ofSeconds(2));
	}
}
