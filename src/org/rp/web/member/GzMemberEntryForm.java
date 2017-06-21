package org.rp.web.member;

import java.io.Serializable;

import org.rp.baseuser.GzProfile;

public class GzMemberEntryForm implements Serializable{

	private static final long serialVersionUID = 7734535183669300952L;
	private GzProfile inCompleteProfile;
	private GzProfile profile;
	private String vPassword;
	private String errMsg;
	private String infoMsg;
	
	public GzMemberEntryForm()
	{
		inCompleteProfile = new GzProfile();
	}

	public GzProfile getInCompleteProfile() {
		return inCompleteProfile;
	}

	public void setInCompleteProfile(GzProfile inCompleteProfile) {
		this.inCompleteProfile = inCompleteProfile;
	}

	public GzProfile getProfile() {
		return profile;
	}

	public void setProfile(GzProfile profile) {
		this.profile = profile;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public String getvPassword() {
		return vPassword;
	}

	public void setvPassword(String vPassword) {
		this.vPassword = vPassword;
	}

}
