package com.seneca.problem.exception;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class DataValidationException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 2619912315322949762L;
	private String fieldName;
	private String errorMessage;
	private Throwable exception;
	
	public DataValidationException(String fieldName, String errorMessage, Throwable exception) {
		super();
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
		this.exception = exception;
	}

	public DataValidationException(String fieldName, String errorMessage) {
		super();
		this.fieldName = fieldName;
		this.errorMessage = errorMessage;
	}
	
	
}
