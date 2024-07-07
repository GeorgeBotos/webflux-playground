package com.botos.webfluxplayground.sec06.config;

import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.HandlerFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import java.util.function.IntBinaryOperator;

@Service
public class CalculatorRequestHandler {

	public HandlerFunction<ServerResponse> handle(IntBinaryOperator function) {
		return request -> {
			int a = Integer.parseInt(request.pathVariable("a"));
			int b = Integer.parseInt(request.pathVariable("b"));
			return ServerResponse.ok()
			                     .bodyValue(function.applyAsInt(a, b));
		};
	}
}
