package com.botos.webfluxplayground.sec09.service;

import com.botos.webfluxplayground.sec09.domain.Product;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("products")
public class ProductController {

	private final ProductService productService;

	@PostMapping
	public Mono<Product> saveProduct(@RequestBody Mono<Product> productMono) {
		return productService.saveProduct(productMono);
	}

	@GetMapping(value = "/stream/{maxPrice}",
	            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<Product> productStream(@PathVariable final Integer maxPrice) {
		return productService.productStream()
		                     .filter(product -> product.price() <= maxPrice);
	}
}
