package org.rp.web.adm;

import java.util.ArrayList;
import java.util.List;

import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.baseuser.persistence.GzBaseUserStub;

public class GzMemberSummary {

	private String weChatName;
	private String userName;
	private String rank;
	private GzMemberSummary parent;
	private List<GzMemberSummary> members = new ArrayList<GzMemberSummary>();
	private boolean enabled;
	
	public GzMemberSummary() {
	}
	
	public GzMemberSummary(GzBaseUser bu)
	{
		setWeChatName(bu.getContact());
		setUserName(bu.getEmail());
		setRank(bu.getRole().getShortCode());
		if (bu.getParent()!=null)
			setParent(new GzMemberSummary(bu.getParent()));
		setEnabled(bu.isEnabled());
	}

	public GzMemberSummary(GzBaseUserStub bu) {
		setWeChatName(bu.getContact());
		setUserName(bu.getEmail());
		setRank(GzRole.valueOf(bu.getRole()).getShortCode());
		GzMemberSummary parent = new GzMemberSummary();
		parent.setWeChatName(bu.getParentcontact());
		parent.setUserName(bu.getParentemail());
		parent.setRank(GzRole.valueOf(bu.getParentrole()).getShortCode());
		setParent(parent);
		setEnabled(bu.isEnabled());
	}

	public String getWeChatName() {
		return weChatName;
	}

	public void setWeChatName(String weChatName) {
		this.weChatName = weChatName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getRank() {
		return rank;
	}

	public void setRank(String rank) {
		this.rank = rank;
	}

	public GzMemberSummary getParent() {
		return parent;
	}

	public void setParent(GzMemberSummary parent) {
		this.parent = parent;
	}

	public List<GzMemberSummary> getMembers() {
		return members;
	}

	public void setMembers(List<GzMemberSummary> members) {
		this.members = members;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

}
