package com.botos.webfluxplayground.sec09.service;

import com.botos.webfluxplayground.sec09.domain.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RequiredArgsConstructor
@Service
public class DataSetupService implements CommandLineRunner {

	private final ProductService productService;

	@Override
	public void run(String... args) throws Exception {
		Flux.range(1, 1_000)
		    .delayElements(Duration.ofSeconds(1))
		    .map(index -> new Product(null, "product-" + index, 100 + index))
		    .map(Mono::just)
		    .flatMap(productService::saveProduct)
		    .subscribe();
	}
}
