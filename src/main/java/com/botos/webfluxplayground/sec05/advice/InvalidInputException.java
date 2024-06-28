package com.botos.webfluxplayground.sec05.advice;

public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
		super(message);
	}
}
