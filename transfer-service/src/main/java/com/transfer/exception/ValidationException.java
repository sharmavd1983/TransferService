package com.transfer.exception;

public class ValidationException extends RuntimeException {
	
	private static final long serialVersionUID = -4198284497406150509L;
	
	public ValidationException(String errorMessage) {
		super(errorMessage);
	}

}
