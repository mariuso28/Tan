package org.rp.baseuser;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.rp.account.GzAccount;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

public class GzBaseUser 
{
	private UUID id;
	private String parentCode;
	private String contact;
	private String email;
	private String phone;
	private String password;
	private String nickname;
	private GzAccount account; 
	private String code;
	private String ip;
	private boolean enabled;
	private GzRole role;
	private List<GzRole> authorities = new ArrayList<GzRole>();
	private String session;
	private List<GzBaseUser> members  = new ArrayList<GzBaseUser>();
	private String icon;
	private GzBaseUser parent;
	private boolean systemMember;						// house bankers etc.
	private long leafInstance;
	
	public GzBaseUser()
	{
		
	}
	
	public GzBaseUser(String email)
	{
		setEmail(email);
		setEnabled(true);
	}
	
	public void copyProfileValues(GzProfile profile)
	{
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		setPassword(encoder.encode(profile.getPassword()));
		setContact(profile.getContact());
		setNickname(profile.getNickname());
		setIcon(profile.getIcon());
		setPhone(profile.getPhone());
	}
	
	public GzProfile createProfile()
	{
		GzProfile profile = new GzProfile();
		profile.setNickname(getNickname());
		profile.setPhone(getPhone());
		profile.setContact(getContact());
		profile.setIcon(getIcon());
		return profile;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		GzBaseUser bu = (GzBaseUser) obj;
		return this.getId().equals(bu.getId());
	}
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public GzAccount getAccount() {
		return account;
	}

	public void setAccount(GzAccount account) {
		this.account = account;
		account.setBaseUser(this);
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public GzRole getRole() {
		return role;
	}

	public void setRole(GzRole role) {
		this.role = role;
	}

	public List<GzRole> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(List<GzRole> authorities) {
		this.authorities = authorities;
	}

	public String getSession() {
		return session;
	}

	public void setSession(String session) {
		this.session = session;
	}

	public List<GzBaseUser> getMembers() {
		return members;
	}

	public void setMembers(List<GzBaseUser> members) {
		this.members = members;
	}

	public String getIcon() {
		return icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public GzBaseUser getParent() {
		return parent;
	}

	public void setParent(GzBaseUser parent) {
		this.parent = parent;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public boolean isSystemMember() {
		return systemMember;
	}

	public void setSystemMember(boolean systemMember) {
		this.systemMember = systemMember;
	}

	public long getLeafInstance() {
		return leafInstance;
	}

	public void setLeafInstance(long leafInstance) {
		this.leafInstance = leafInstance;
	}
	
}
