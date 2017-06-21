package org.rp.account.persistence;

public class UserCodeMap
{
	private String email;
	private String code;
	private String role;
	
	public UserCodeMap()
	{
		
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	public String getCode() {
		return code;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getRole() {
		return role;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
}
