package com.botos.webfluxplayground.sec06.advice;

public class InvalidInputException extends RuntimeException {

	public InvalidInputException(String message) {
		super(message);
	}
}
