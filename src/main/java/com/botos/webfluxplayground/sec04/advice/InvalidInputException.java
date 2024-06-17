package com.botos.webfluxplayground.sec04.advice;

public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
		super(message);
	}
}
