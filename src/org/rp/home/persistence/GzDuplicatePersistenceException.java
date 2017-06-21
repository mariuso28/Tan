package org.rp.home.persistence;

public class GzDuplicatePersistenceException extends GzPersistenceException {

	private static final long serialVersionUID = -560262451620708358L;

	public GzDuplicatePersistenceException(String field,String message) {
		super("Duplicate on : " + field + " - " + message);
	}
	
	public GzDuplicatePersistenceException(String message) {
		super(message);
	}

}
