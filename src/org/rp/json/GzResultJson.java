package org.rp.json;

public class GzResultJson {

	private GzStatusJson status;
	private String message;
	private Object result;

	public GzResultJson() {}

	public void fail(String message)
	{
		setStatus(GzStatusJson.ERROR);
		setMessage(message);
	}
	
	public void warn(String message)
	{
		setStatus(GzStatusJson.WARN);
		setMessage(message);
	}
	
	public void success(Object result)
	{
		setStatus(GzStatusJson.OK);
		setResult(result);
	}
	
	public void success()
	{
		setStatus(GzStatusJson.OK);
	}
	
	public GzStatusJson getStatus() {
		return status;
	}

	public void setStatus(GzStatusJson status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}	
}