package com.botos.webfluxplayground.sec08;

import com.botos.webfluxplayground.sec08.domain.Product;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;

class ProductsUploadDownloadTest {

	private final static Logger log = LoggerFactory.getLogger(ProductsUploadDownloadTest.class);

	private final ProductClient productClient = new ProductClient();

	@Test
	void upload() {
		var productFlux = Flux.range(1, 10)
		                      .map(index -> new Product(null, "product-" + index, 1000 + index))
		                      .delayElements(Duration.ofSeconds(2));
		productClient.uploadProducts(productFlux)
		             .doOnNext(uploadResponse -> log.info("received: {}", uploadResponse))
		             .then()
		             .as(StepVerifier::create)
		             .verifyComplete();
	}

	@Test
	void download() {
		productClient.downloadProducts()
		             .doOnNext(product -> log.info("received product: {}", product))
		             .as(StepVerifier::create)
		             .expectNextCount(10)
		             .verifyComplete();
	}
}
