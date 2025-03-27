package com.mvc.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class UserExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String,String>> hasFieldErrors(MethodArgumentNotValidException exception){
		Map<String,String> errorMap = new HashMap<>();
		exception.getFieldErrors().forEach(error->{
			errorMap.put(error.getField(),error.getDefaultMessage());
		});
		return ResponseEntity.badRequest().body(errorMap);
	}
	
	@ExceptionHandler(UserNotFoundException.class)
	public ResponseEntity<Map<String,String>> userNotFoundError(UserNotFoundException exception){
		Map<String,String> errorMap = new HashMap<>();
		errorMap.put("Error",exception.getMessage());
		return new ResponseEntity<Map<String,String>>(errorMap, HttpStatus.NOT_FOUND);
	}
}//Exception