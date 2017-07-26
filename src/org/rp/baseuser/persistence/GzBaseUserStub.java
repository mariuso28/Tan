package org.rp.baseuser.persistence;

public class GzBaseUserStub {

	private String email;
	private String contact;
	private String role;
	private String parentemail;
	private String parentcontact;
	private String parentrole;
	private boolean enabled;
	
	public GzBaseUserStub()
	{
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public String getParentemail() {
		return parentemail;
	}

	public void setParentemail(String parentemail) {
		this.parentemail = parentemail;
	}

	public String getParentcontact() {
		return parentcontact;
	}

	public void setParentcontact(String parentcontact) {
		this.parentcontact = parentcontact;
	}

	public String getParentrole() {
		return parentrole;
	}

	public void setParentrole(String parentrole) {
		this.parentrole = parentrole;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	
}
