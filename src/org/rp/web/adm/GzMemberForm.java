package org.rp.web.adm;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class GzMemberForm implements Serializable {

	private static final long serialVersionUID = -9110917883160354671L;
	private GzMemberCommand command;
	private GzMemberCommand inCompleteCommand;
	private List<GzMemberSummary> upstreamMembers = new ArrayList<GzMemberSummary>();
	private List<GzMemberSummary> flatMembers = new ArrayList<GzMemberSummary>();
	private List<GzMemberSummary> possibleSuperiors = new ArrayList<GzMemberSummary>();
	private List<GzMemberSummary> uplineMembers;
	private List<GzMemberSummary> chooseMembers;
	private boolean adminOnly;
	private String errMsg;
	private String infoMsg;
	private GzMemberSummary memberSummary;
	
	public GzMemberForm()
	{
	}

	public List<GzMemberSummary> getUpstreamMembers() {
		return upstreamMembers;
	}


	public void setUpstreamMembers(List<GzMemberSummary> upstreamMembers) {
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

	public GzMemberSummary getMemberSummary() {
		return memberSummary;
	}

	public void setMemberSummary(GzMemberSummary memberSummary) {
		this.memberSummary = memberSummary;
	}

	public List<GzMemberSummary> getFlatMembers() {
		return flatMembers;
	}

	public void setFlatMembers(List<GzMemberSummary> flatMembers) {
		this.flatMembers = flatMembers;
	}

	public List<GzMemberSummary> getPossibleSuperiors() {
		return possibleSuperiors;
	}

	public void setPossibleSuperiors(List<GzMemberSummary> possibleSuperiors) {
		this.possibleSuperiors = possibleSuperiors;
	}

	public List<GzMemberSummary> getUplineMembers() {
		return uplineMembers;
	}

	public void setUplineMembers(List<GzMemberSummary> uplineMembers) {
		this.uplineMembers = uplineMembers;
	}

	public List<GzMemberSummary> getChooseMembers() {
		return chooseMembers;
	}

	public void setChooseMembers(List<GzMemberSummary> chooseMembers) {
		this.chooseMembers = chooseMembers;
	}

	
}
