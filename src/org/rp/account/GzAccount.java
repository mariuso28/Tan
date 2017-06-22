package org.rp.account;

import java.util.UUID;

import org.apache.log4j.Logger;
import org.rp.baseuser.GzBaseUser;

public class GzAccount 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAccount.class);	
	private GzBaseUser baseUser;
	private double balance;
	private double playerRoyalty;
	private double bankerRoyalty;
	private double totalPlayer;
	private double totalBanker;
	private double distributePlayer;
	private double distributeBanker;
	private UUID transactionMatchId;
	
	public GzAccount()
	{
	}
	
	public void initiazeTotals() {
		setTotalPlayer(0);
		setTotalBanker(0);
		setDistributePlayer(0);
		setDistributeBanker(0);
	}
	
	public void updateDistributions() {
		setDistributePlayer((getTotalPlayer()*getPlayerRoyalty())/100.0);
		setDistributeBanker((getTotalBanker()*getBankerRoyalty())/100.0);
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

	public double getPlayerRoyalty() {
		return playerRoyalty;
	}

	public void setPlayerRoyalty(double playerRoyalty) {
		this.playerRoyalty = playerRoyalty;
	}

	public double getBankerRoyalty() {
		return bankerRoyalty;
	}

	public void setBankerRoyalty(double bankerRoyalty) {
		this.bankerRoyalty = bankerRoyalty;
	}

	public double getDistributePlayer() {
		return distributePlayer;
	}

	public void setDistributePlayer(double distributePlayer) {
		this.distributePlayer = distributePlayer;
	}

	public double getDistributeBanker() {
		return distributeBanker;
	}

	public void setDistributeBanker(double distributeBanker) {
		this.distributeBanker = distributeBanker;
	}

	public double getTotalPlayer() {
		return totalPlayer;
	}

	public void setTotalPlayer(double totalPlayer) {
		this.totalPlayer = totalPlayer;
	}

	public double getTotalBanker() {
		return totalBanker;
	}

	public void setTotalBanker(double totalBanker) {
		this.totalBanker = totalBanker;
	}

	public UUID getTransactionMatchId() {
		return transactionMatchId;
	}

	public void setTransactionMatchId(UUID transactionMatchId) {
		this.transactionMatchId = transactionMatchId;
	}

	

 }
