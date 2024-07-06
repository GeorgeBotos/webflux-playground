package com.botos.webfluxplayground.sec06.advice;

import com.botos.webfluxplayground.sec06.advice.CustomerNotFoundException;
import com.botos.webfluxplayground.sec06.advice.InvalidInputException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.util.function.Consumer;

@Service
public class CustomerExceptionHandler {


	public Mono<ServerResponse> handleException(CustomerNotFoundException exception, ServerRequest request) {
		return handleException(HttpStatus.NOT_FOUND, exception, request, problemDetail -> {
			problemDetail.setType(URI.create("http://example.com/problems/customer-not-found"));
			problemDetail.setTitle("Customer Not Found");
		});
	}


	public Mono<ServerResponse> handleException(InvalidInputException exception, ServerRequest request) {
		return handleException(HttpStatus.BAD_REQUEST, exception, request, problemDetail -> {
			problemDetail.setType(URI.create("http://example.com/problems/customer-not-found"));
			problemDetail.setTitle("Invalid Input");
		});
	}

	private Mono<ServerResponse> handleException(HttpStatus status, Exception exception, ServerRequest request, Consumer<ProblemDetail> problemDetailConsumer) {
		var problemDetail = ProblemDetail.forStatusAndDetail(status, exception.getMessage());
		problemDetailConsumer.accept(problemDetail);
		problemDetail.setInstance(URI.create(request.path()));
		return ServerResponse.status(status)
		                     .bodyValue(problemDetail);
	}
}
