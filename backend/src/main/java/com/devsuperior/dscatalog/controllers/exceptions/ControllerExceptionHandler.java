package com.devsuperior.dscatalog.controllers.exceptions;

import java.time.Instant;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class ControllerExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<StandardError> entityNotFound(ResourceNotFoundException ex, HttpServletRequest request) {
		StandardError error = new StandardError();
		
		HttpStatus status = HttpStatus.NOT_FOUND;
		
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Resource not found");
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(DatabaseException.class)
	public ResponseEntity<StandardError> database(DatabaseException ex, HttpServletRequest request) {
		StandardError error = new StandardError();
		
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Database exception");
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		return ResponseEntity.status(status).body(error);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ValidationError> validation(MethodArgumentNotValidException ex, HttpServletRequest request) {
		ValidationError error = new ValidationError();
		
		HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
		
		error.setTimestamp(Instant.now());
		error.setStatus(status.value());
		error.setError("Validation exception");
		error.setMessage(ex.getMessage());
		error.setPath(request.getRequestURI());
		
		for(FieldError field : ex.getBindingResult().getFieldErrors()) {
			error.addError(field.getField(), field.getDefaultMessage());
		}
		
		return ResponseEntity.status(status).body(error);
	}
}
