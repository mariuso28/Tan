package org.rp.framework;

public class GzMailException extends Exception {

	private static final long serialVersionUID = 7037839127622579460L;
	private Exception e;
	
	public GzMailException(String message,Exception e)
	{
		super("Error on email : " + message);
		setE(e);
	}

	public GzMailException(String message)
	{
		super("Error on email : " + message);
	}
	
	public Exception getE() {
		return e;
	}

	public void setE(Exception e) {
		this.e = e;
	}

	@Override
	public String toString() {
		return "PkfzHomeStorageException [e=" + e + "]";
	}
	

}
