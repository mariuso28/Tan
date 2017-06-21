package org.rp.account;

import org.rp.baseuser.GzRole;

public class GzRollup {
	
	private String code;
	private String email;
	private String role;
	private double paidIn;
	private double paidOut;
	private double deposit;
	private double withdrawl;
	private double owed;
	private double owing;
	private double total;
	private double totalOwed;
	
	public GzRollup(String email,String code,GzRole role)
	{
		setEmail(email);
		setCode(code);
		setRole(role.getShortCode());
	}
	
	public boolean isEmpty()
	{
		return paidIn==0.0 && paidOut==0.0 && 
			deposit==0.0 &&  
			withdrawl==0.0 && owed==0.0 && owing==0;
	}
	
	public void calcTotal()
	{
		total = paidIn - paidOut + deposit - withdrawl;
		totalOwed = owed - owing;
	}
	
	public double getPaidIn() {
		return paidIn;
	}
	public void setPaidIn(double paidIn) {
		this.paidIn = paidIn;
	}
	public double getPaidOut() {
		return paidOut;
	}
	public void setPaidOut(double paidOut) {
		this.paidOut = paidOut;
	}
	
	public double getDeposit() {
		return deposit;
	}
	public void setDeposit(double deposit) {
		this.deposit = deposit;
	}
	public double getWithdrawl() {
		return withdrawl;
	}
	public void setWithdrawl(double withdrawl) {
		this.withdrawl = withdrawl;
	}
	
	public void setTotal(double total) {
		this.total = total;
	}
	public double getTotal() {
		return total;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRole() {
		return role;
	}

	public double getOwed() {
		return owed;
	}

	public void setOwed(double owed) {
		this.owed = owed;
	}

	public double getOwing() {
		return owing;
	}

	public void setOwing(double owing) {
		this.owing = owing;
	}

	public double getTotalOwed() {
		return totalOwed;
	}

	public void setTotalOwed(double totalOwed) {
		this.totalOwed = totalOwed;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	
}
