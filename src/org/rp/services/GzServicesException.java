package org.rp.services;

public class GzServicesException extends Exception {


	private static final long serialVersionUID = 6390011278563732021L;

	public GzServicesException(String message)
	{
		super("GzServicesException - " + message);
	}
}
