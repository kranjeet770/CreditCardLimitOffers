package com.credit.home.exceptions;


public class ResourceAlreadyExistsException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistsException(String msg) 
	{
	    super(msg);
	}
}