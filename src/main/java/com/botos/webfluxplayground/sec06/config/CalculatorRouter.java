package com.botos.webfluxplayground.sec06.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.Objects;

@RequiredArgsConstructor
@Configuration
public class CalculatorRouter {

	private final CalculatorRequestHandler calculatorRequestHandler;

	@Bean
	public RouterFunction<ServerResponse> routeCalculator() {
		return RouterFunctions.route()
		                      .filter(((request, next) -> Objects.equals(request.pathVariable("b"), "0")
		                                                  ? ServerResponse.badRequest()
		                                                                  .bodyValue("The second parameter cannot be null")
		                                                  : next.handle(request)))
		                      .path("calculator/{a}/{b}", this::routeOperation)
		                      .build();
	}

	private RouterFunction<ServerResponse> routeOperation() {
		return RouterFunctions.route()
		                      .GET(isOperation("+"), calculatorRequestHandler.handle(Integer::sum))
		                      .GET(isOperation("-"), calculatorRequestHandler.handle((a, b) -> a - b))
		                      .GET(isOperation("*"), calculatorRequestHandler.handle((a, b) -> a * b))
		                      .GET(isOperation("/"), calculatorRequestHandler.handle((a, b) -> a / b))
		                      .GET(badRequest())
		                      .build();
	}

	private RequestPredicate isOperation(String operation) {
		return RequestPredicates.headers(headers -> Objects.equals(headers.firstHeader("operation"), operation));
	}

	private HandlerFunction<ServerResponse> badRequest() {
		return request -> ServerResponse.badRequest()
		                                .bodyValue("operation header should be + - * /");
	}
}
