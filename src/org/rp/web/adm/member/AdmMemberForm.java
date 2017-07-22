package org.rp.web.adm.member;

import java.io.Serializable;

public class AdmMemberForm implements Serializable{

	private static final long serialVersionUID = 1725796840094304131L;
	private String password;
	private String vPassword;
	private String errMsg;
	private String infoMsg;
	
	public AdmMemberForm()
	{
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getvPassword() {
		return vPassword;
	}

	public void setvPassword(String vPassword) {
		this.vPassword = vPassword;
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

}
