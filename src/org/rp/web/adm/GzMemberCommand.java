package org.rp.web.adm;

import java.io.Serializable;

public class GzMemberCommand implements Serializable {

	private static final long serialVersionUID = 6773672023960627190L;
	private String username;
	private String memberRank;
	private String password;
	private String vPassword;
	private String weChatName;
	private String superiorCode;

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

	@Override
	public String toString() {
		return "GzMemberCommand [username=" + username + ", memberRank=" + memberRank + ", password=" + password
				+ ", vPassword=" + vPassword + ", weChatName=" + weChatName + ", superiorCode=" + superiorCode + "]";
	}

	
}
