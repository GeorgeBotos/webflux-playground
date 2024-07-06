package com.botos.webfluxplayground.sec06.config;

import com.botos.webfluxplayground.sec06.advice.CustomerExceptionHandler;
import com.botos.webfluxplayground.sec06.advice.CustomerNotFoundException;
import com.botos.webfluxplayground.sec06.advice.InvalidInputException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@RequiredArgsConstructor
@Configuration
public class RouterConfiguration {

	private final CustomerRequestHandler customerRequestHandler;

	private final CustomerExceptionHandler exceptionHandler;

	@Bean
	public RouterFunction<ServerResponse> routeCustomer() {
		return RouterFunctions.route()
		                      .GET("customers", customerRequestHandler::getAllCustomers)
		                      .GET("customers/paginated", customerRequestHandler::getPaginatedCustomers)
		                      .GET("customers/{id}", customerRequestHandler::getCustomer)
		                      .POST("customers", customerRequestHandler::saveCustomer)
		                      .PUT("customers/{id}", customerRequestHandler::updateCustomer)
		                      .DELETE("customers/{id}", customerRequestHandler::deleteCustomer)
		                      .onError(CustomerNotFoundException.class, exceptionHandler::handleException)
		                      .onError(InvalidInputException.class, exceptionHandler::handleException)
		                      .build();
	}
}
