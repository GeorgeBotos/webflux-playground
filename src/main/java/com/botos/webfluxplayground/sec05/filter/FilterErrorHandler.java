package com.botos.webfluxplayground.sec05.filter;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FilterErrorHandler {

	private final ServerCodecConfigurer codecConfigurer;

	private ServerResponse.Context context;

	@PostConstruct
	private void init() {
		context = new ContextImpl(codecConfigurer);
	}

	public Mono<Void> sendProblemDetails(ServerWebExchange serverwebExchange, HttpStatus httpStatus, String message) {
		var problemDetails = ProblemDetail.forStatusAndDetail(httpStatus, message);
		return ServerResponse.status(httpStatus)
		                     .bodyValue(problemDetails)
		                     .flatMap(serverResponse -> serverResponse.writeTo(serverwebExchange, context));
	}

	private record ContextImpl(ServerCodecConfigurer codecConfigurer) implements ServerResponse.Context {

		@Override
		public List<HttpMessageWriter<?>> messageWriters() {
			return codecConfigurer.getWriters();
		}

		@Override
		public List<ViewResolver> viewResolvers() {
			return List.of();
		}
	}

}
