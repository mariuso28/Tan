package org.rp.account;

import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.log4j.Logger;
import org.rp.agent.GzAgent;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;

public class GzAccountMgr {

	protected static Logger log = Logger.getLogger(GzAccountMgr.class);
	private GzServices services;
	private GzHome home;
	
	public GzAccountMgr()
	{
	}
	
	public void initiazeTotals(String baseComp) throws GzPersistenceException {
	
		GzAgent topAgent = (GzAgent) home.getAgentByEmail(baseComp);
		topAgent.getAccount().initiazeTotals();
	}
	
	public void createTransactionsForTurnovers(GzBaseUser player,double turnover,double bankerTurnover,String source) throws GzPersistenceException
	{
		log.info("Creating transaction turnovers for : " + player + " source: " + source);
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		GzAgent topAgent = (GzAgent) getMemberChain(player);	
		GzTransaction transaction = new GzTransaction(topAgent.getEmail(),player.getEmail(),GzTransaction.BANKERTURNOVER,turnover,now,source);
		home.storeTransaction(transaction);
		transaction = new GzTransaction(topAgent.getEmail(),player.getEmail(),GzTransaction.PLAYERTURNOVER,bankerTurnover,now,source);
		home.storeTransaction(transaction);
		
		GzAccount account = topAgent.getAccount();
		
		account.setTotalPlayer(account.getTotalPlayer()+turnover);
		account.setTotalBanker(account.getTotalBanker()+bankerTurnover);
		home.updateDistributions(account);
	}
	
	public void setDistributions(String baseComp) throws GzPersistenceException
	{
		// set default distributions
		GzAgent topAgent = (GzAgent) home.getAgentByEmail(baseComp);
		GzAccount account = topAgent.getAccount();
		account.updateDistributions();
		home.updateDistributions(account);
	}
	
	public void createRoyalties(String baseComp) throws GzPersistenceException
	{
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		GzAgent topAgent = (GzAgent) home.getAgentByEmail(baseComp);
		GzAccount account = topAgent.getAccount();
		for (GzBaseUser bu : topAgent.getMembers())								// SMA
		{
			if (account.getDistributePlayer()>0)
				createRoyaltyForPlayer(topAgent,bu,account.getDistributePlayer(),now,null);
			if (account.getDistributeBanker()>0)
				createRoyaltyForBanker(topAgent,bu,account.getDistributeBanker(),now,null);
		}
	}
	
	private void createRoyaltyForPlayer(GzBaseUser parent,GzBaseUser baseUser,double amount,Date now, GzInvoice parentInvoice) throws GzPersistenceException
	{	
		double royalty = baseUser.getAccount().getPlayerRoyalty();
		double netAmount = (amount * royalty) /100.0;
		long parentInvoiceId = -1;
		if (parentInvoice != null)
			parentInvoiceId = parentInvoice.getId();
			
		GzInvoice invoice = new GzInvoice(parent.getEmail(),baseUser.getEmail(),amount,royalty,netAmount,now,parentInvoiceId);
		invoice = home.storeInvoice(invoice);
		home.getMembersForBaseUser(baseUser);
		for (GzBaseUser bu : baseUser.getMembers())
		{
			createRoyaltyForPlayer(baseUser,bu,amount,now,invoice);
		}
	}
	
	private void createRoyaltyForBanker(GzBaseUser parent,GzBaseUser baseUser,double amount,Date now, GzInvoice parentInvoice) throws GzPersistenceException
	{
		double royalty = baseUser.getAccount().getBankerRoyalty();
		double netAmount = (amount * royalty) / 100.0;
		long parentInvoiceId = -1;
		if (parentInvoice != null)
			parentInvoiceId = parentInvoice.getId();
	
		GzInvoice invoice = new GzInvoice(parent.getEmail(),baseUser.getEmail(),amount,royalty,netAmount,now,parentInvoiceId);
		invoice = home.storeInvoice(invoice);
		home.getMembersForBaseUser(baseUser);
		for (GzBaseUser bu : baseUser.getMembers())
		{
			createRoyaltyForBanker(baseUser,bu,amount,now,invoice);
		}
	}
	
	private GzBaseUser getMemberChain(GzBaseUser bu) throws GzPersistenceException
	{
		GzBaseUser parent = bu;
		while (bu.getParentCode().charAt(0) != GzRole.ROLE_ADMIN.getCode())
		{
			parent = bu.getParent();
			if (parent == null)												// get from db
			{
				parent = home.getAgentByCode(bu.getParentCode());
				bu.setParent(parent);
			}
			bu = parent;
		}
		return parent;
	}
	
	public GzHome getHome() {
		return home;
	}

	public void setHome(GzHome home) {
		this.home = home;
	}

	public GzServices getServices() {
		return services;
	}

	public void setServices(GzServices services) {
		this.services = services;
	}

	
}
