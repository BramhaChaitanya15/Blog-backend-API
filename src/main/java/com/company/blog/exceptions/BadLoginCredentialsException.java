package com.company.blog.exceptions;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BadLoginCredentialsException extends RuntimeException {

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = 229077826090224814L;
	String resourceName;
	String fieldName;
	String fieldValue;
	
	public BadLoginCredentialsException(String resourceName, String fieldName, String fieldValue) {
		super(String.format("%s not found with %s : %s", resourceName, fieldName, fieldValue));
		this.resourceName = resourceName;
		this.fieldName = fieldName;
		this.fieldValue = fieldValue;
	}
	
	public BadLoginCredentialsException(String msg) {
		super(msg);
	}
		
}
