package org.rp.account;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;

public class GzAccount 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAccount.class);	
	private GzBaseUser baseUser;
	private double balance;
	private double commission;
	
	
	public GzAccount()
	{
	}
	
	public GzAccount(GzBaseUser baseUser)
	{
		setBaseUser(baseUser);
	}
	
	public void updateBalance(double stake) {
		balance += stake;
	}
	
	public void addBalance(double amount)
	{
		balance += amount;
	}
	
	public double getBalance() {
		return balance;
	}

	public GzBaseUser getBaseUser() {
		return baseUser;
	}

	public void setBaseUser(GzBaseUser baseUser) {
		this.baseUser = baseUser;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}

	public double getCommission() {
		return commission;
	}

	public void setCommission(double commission) {
		this.commission = commission;
	}

	

 }
