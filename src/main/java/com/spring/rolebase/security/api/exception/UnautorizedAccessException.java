package com.spring.rolebase.security.api.exception;

public class UnautorizedAccessException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8815946575650086124L;

	public UnautorizedAccessException(String message) {
		super(message);
	}

}
