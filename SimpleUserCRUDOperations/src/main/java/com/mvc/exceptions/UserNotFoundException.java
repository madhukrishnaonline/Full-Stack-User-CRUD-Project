package com.mvc.exceptions;

@SuppressWarnings("serial")
public class UserNotFoundException extends Exception {
	public UserNotFoundException() {
	}

	public UserNotFoundException(String msg) {
		super(msg);
	}
}// class