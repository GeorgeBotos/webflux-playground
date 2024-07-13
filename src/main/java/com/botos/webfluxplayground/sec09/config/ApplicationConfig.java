package com.botos.webfluxplayground.sec09.config;

import com.botos.webfluxplayground.sec09.domain.Product;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class ApplicationConfig {

	@Bean
	public Sinks.Many<Product> sink() {
		return Sinks.many()
		            .replay()
		            .limit(1);
	}
}
