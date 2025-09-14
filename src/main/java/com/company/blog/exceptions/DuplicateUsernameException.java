package com.company.blog.exceptions;

import java.io.Serial;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DuplicateUsernameException extends RuntimeException{

	/**
	 * 
	 */
	@Serial
	private static final long serialVersionUID = -8099322122118815201L;

	public DuplicateUsernameException(String msg) {
		super(msg);
	}
	
	public DuplicateUsernameException() {
		super("Duplicate Username!");
	}

}
