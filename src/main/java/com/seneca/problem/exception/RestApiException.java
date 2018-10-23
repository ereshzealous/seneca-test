package com.seneca.problem.exception;

import org.apache.commons.lang3.Validate;



/**
 * Created by @author Eresh Gorantla on 28-Mar-2018
 */

public class RestApiException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private RestFault fault;

	public RestApiException() {
		super();
	}

	public RestApiException(DataValidationException dve) {
		this.fault = createFault(dve);
	}

	public RestApiException(String message, RestFault wsFault) {
		super(message);
		Validate.notNull(wsFault, "Fault cannot be null.");
		this.fault = wsFault;
	}

	public RestFault getFaultInfo() {
		return this.fault;
	}

	public static RestFault createApplicationExceptionFault(Exception ae) {
		RestFault fault = new RestFault();
		fault.setErrorKey("UnExpected Error");
		fault.setErrorMessage(ae.getMessage());
		fault.setResult("AppException");
		return fault;
	}

	public static RestFault createUnexpectedFault(Exception ex) {
		RestFault fault = new RestFault();
		fault.setResult("AppException");
		fault.setErrorKey("UnExpected Error");
		fault.setErrorMessage(ex.getCause().getMessage());
		//fault.setUid(UUID.randomUUID().toString());

		return fault;
	}
	
	private RestFault createFault(DataValidationException e) {
		RestFault fault = new RestFault();
		fault.setFieldName(e.getFieldName());
		fault.setErrorMessage(e.getErrorMessage());
		fault.setResult("DataValidation Error");
		return fault;
	}

}
