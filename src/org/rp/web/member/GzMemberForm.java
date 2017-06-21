package org.rp.web.member;

import java.io.Serializable;

public class GzMemberForm implements Serializable{

	private static final long serialVersionUID = -6571236167091425616L;
	private String createRole;
	private String infoMsg;
	private String errMsg;
	
	public GzMemberForm()
	{
	}

	public String getCreateRole() {
		return createRole;
	}

	public void setCreateRole(String createRole) {
		this.createRole = createRole;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

}
