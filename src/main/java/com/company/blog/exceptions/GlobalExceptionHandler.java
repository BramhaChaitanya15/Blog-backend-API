package com.company.blog.exceptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.company.blog.payloads.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	// Resource not found exception
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponse> resourceNotFoundExceptionHandler(ResourceNotFoundException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	// wrong login credentials exception
	@ExceptionHandler(BadLoginCredentialsException.class)
	public ResponseEntity<ApiResponse> badLoginCredentialsExceptionHandler(BadLoginCredentialsException ex) {
		String message = ex.getMessage();
		ApiResponse apiResponse = new ApiResponse(message, false);
		return new ResponseEntity<ApiResponse>(apiResponse, HttpStatus.UNAUTHORIZED);
	}

	// VALIDATION EXCEPTIONS
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> methodArgsNotValidExceptionHandler(MethodArgumentNotValidException ex) {
		Map<String, String> res = new HashMap<>();

		ex.getBindingResult().getAllErrors().forEach((error) -> {
			String fieldName = ((FieldError) error).getField();
			String message = error.getDefaultMessage();
			res.put(fieldName, message);
		});

		return new ResponseEntity<Map<String, String>>(res, HttpStatus.BAD_REQUEST);
	}
	
	// IOException handling
	@ExceptionHandler(DuplicateUsernameException.class)
	public ResponseEntity<ApiResponse> duplicateUsernameExceptionHandler(DuplicateUsernameException ex) {
		ApiResponse apiResponse = new ApiResponse(ex.getMessage(), false);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	// IOException handling
	@ExceptionHandler(IOException.class)
	public ResponseEntity<ApiResponse> ioExceptionHandler(IOException ex) {
		ApiResponse apiResponse = new ApiResponse("Somthing Went Wrong, consider cheking your file type and size.", false);
		return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
	// Unique constraint violations (username duplicate)
	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<ApiResponse> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
	    ApiResponse apiResponse = new ApiResponse("Data Integrity Voilation, please check the data before you move on...", false);
	    return new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST);
	}


}
