package org.rp.web.adm;

import java.io.Serializable;

public class GzMemberCommand implements Serializable {

	private static final long serialVersionUID = 6773672023960627190L;
	private String username;
	private String memberRank;
	private String password;
	private String vPassword;
	private String weChatName;
	private String memberToChangeCode;
	private String memberToChangeUpline;
	private String superiorCode;
	private String search;
	private String search1;
	private String search2;
	private boolean enabled;
	
	public GzMemberCommand()
	{
	}
	
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getMemberRank() {
		return memberRank;
	}

	public void setMemberRank(String memberRank) {
		this.memberRank = memberRank;
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

	public String getSuperiorCode() {
		return superiorCode;
	}

	public void setSuperiorCode(String superiorCode) {
		this.superiorCode = superiorCode;
	}

	public String getWeChatName() {
		return weChatName;
	}

	public void setWeChatName(String weChatName) {
		this.weChatName = weChatName;
	}

	public String getMemberToChangeCode() {
		return memberToChangeCode;
	}

	public void setMemberToChangeCode(String memberToChangeCode) {
		this.memberToChangeCode = memberToChangeCode;
	}

	public String getMemberToChangeUpline() {
		return memberToChangeUpline;
	}

	public void setMemberToChangeUpline(String memberToChangeUpline) {
		this.memberToChangeUpline = memberToChangeUpline;
	}

	public String getSearch() {
		return search;
	}

	public void setSearch(String search) {
		this.search = search;
	}
	
	public String getSearch1() {
		return search1;
	}

	public void setSearch1(String search1) {
		this.search1 = search1;
	}

	public String getSearch2() {
		return search2;
	}

	public void setSearch2(String search2) {
		this.search2 = search2;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "GzMemberCommand [username=" + username + ", memberRank=" + memberRank + ", password=" + password
				+ ", vPassword=" + vPassword + ", weChatName=" + weChatName + ", memberToChangeCode="
				+ memberToChangeCode + ", memberToChangeUpline=" + memberToChangeUpline + ", superiorCode="
				+ superiorCode + ", search=" + search + ", search1=" + search1 + ", search2=" + search2 + ", enabled="
				+ enabled + "]";
	}

}
