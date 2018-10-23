package com.seneca.problem.exception;

/**
 * Created by @author Eresh Gorantla on 28-Mar-2018
 */

public class RestFaultResponse {
	protected RestFault fault;

	public RestFault getFault() {
		return fault;
	}

	public void setFault(RestFault fault) {
		this.fault = fault;
	}
}

