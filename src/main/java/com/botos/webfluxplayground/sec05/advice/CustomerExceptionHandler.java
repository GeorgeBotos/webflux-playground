package com.botos.webfluxplayground.sec05.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.net.URI;

@ControllerAdvice
public class CustomerExceptionHandler {

	@ExceptionHandler(CustomerNotFoundException.class)
	public ProblemDetail handleException(CustomerNotFoundException exception) {
		var problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, exception.getMessage());
		problem.setType(URI.create("http://example.com/problems/customer-not-found"));
		problem.setTitle("Customer Not Found");
		return problem;
	}

	@ExceptionHandler(InvalidInputException.class)
	public ProblemDetail handleException(InvalidInputException exception) {
		var problem = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, exception.getMessage());
		problem.setType(URI.create("http://example.com/problems/invalid-input"));
		problem.setTitle("Invalid Input");
		return problem;
	}
}
