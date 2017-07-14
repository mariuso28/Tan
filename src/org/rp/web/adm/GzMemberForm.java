package org.rp.web.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.rp.baseuser.GzBaseUser;

public class GzMemberForm implements Serializable {

	private static final long serialVersionUID = -9110917883160354671L;
	private GzMemberCommand command;
	private GzMemberCommand inCompleteCommand;
	private List<GzBaseUser> upstreamMembers = new ArrayList<GzBaseUser>();
	private boolean adminOnly;
	private String errMsg;
	private String infoMsg;
	
	public GzMemberForm()
	{
	}

	public List<GzBaseUser> getUpstreamMembers() {
		return upstreamMembers;
	}

	public void setUpstreamMembers(List<GzBaseUser> upstreamMembers) {
		this.upstreamMembers = upstreamMembers;
	}

	public GzMemberCommand getCommand() {
		return command;
	}

	public void setCommand(GzMemberCommand command) {
		this.command = command;
	}

	public GzMemberCommand getInCompleteCommand() {
		return inCompleteCommand;
	}

	public void setInCompleteCommand(GzMemberCommand inCompleteCommand) {
		this.inCompleteCommand = inCompleteCommand;
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

	public boolean isAdminOnly() {
		return adminOnly;
	}

	public void setAdminOnly(boolean adminOnly) {
		this.adminOnly = adminOnly;
	}

	
}
