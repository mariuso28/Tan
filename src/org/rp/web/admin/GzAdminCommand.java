package org.rp.web.admin;

import java.io.Serializable;

import org.rp.baseuser.GzProfile;

public class GzAdminCommand implements Serializable
{
	private static final long serialVersionUID = -804903870498969809L;
	private String verifyPassword;
	private GzProfile profile;

	public GzAdminCommand()
	{
	}

	public String getVerifyPassword() {
		return verifyPassword;
	}

	public void setVerifyPassword(String verifyPassword) {
		this.verifyPassword = verifyPassword;
	}

	public GzProfile getProfile() {
		return profile;
	}

	public void setProfile(GzProfile profile) {
		this.profile = profile;
	}

}
