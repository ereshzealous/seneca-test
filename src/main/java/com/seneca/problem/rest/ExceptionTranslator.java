package com.seneca.problem.rest;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.seneca.problem.exception.DataValidationException;
import com.seneca.problem.exception.RestApiException;
import com.seneca.problem.exception.RestFault;
import com.seneca.problem.exception.RestFaultResponse;

@ControllerAdvice
public class ExceptionTranslator {
	
	@ExceptionHandler(Exception.class)
    protected ResponseEntity<Object> handleException(Throwable ex, WebRequest request) {
		RestApiException restApiException = null;

		if (ex instanceof RestApiException) {
			restApiException = (RestApiException) ex;
		} else if (ex instanceof DataValidationException) {
			restApiException = new RestApiException((DataValidationException) ex);
		} 

		if (restApiException == null) {
			restApiException = new RestApiException(ex.getMessage(), RestApiException.createUnexpectedFault(new Exception(ex)));
		}
		
		RestFault fault = restApiException.getFaultInfo();
		RestFaultResponse faultResponse = new RestFaultResponse();
		faultResponse.setFault(fault);
		HttpHeaders headers = new HttpHeaders();
		headers.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
		return new ResponseEntity<Object>(fault, headers, HttpStatus.valueOf(500));
	}
}
