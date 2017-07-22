package org.rp.account;

import org.apache.log4j.Logger;
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
	/*
	public void createTransactionsForTurnovers(GzBaseUser player,double turnover,double bankerTurnover,String source) throws GzPersistenceException
	{
		if (turnover==0 && bankerTurnover==0)
			return;
				
		log.info("Creating transaction turnovers for : " + player + " source: " + source);
		GregorianCalendar gc = new GregorianCalendar();
		Date now = gc.getTime();
		GzAgent topAgent = (GzAgent) getMemberChain(player);	
		if (turnover!=0)
		{
			GzTransaction transaction = new GzTransaction(topAgent.getEmail(),player.getEmail(),GzTransaction.PLAYERTURNOVER,turnover,now,source,player.getAccount().getTransactionMatchId());
			home.storeTransaction(transaction);
		}
		if (bankerTurnover!=0)
		{
			GzTransaction transaction = new GzTransaction(topAgent.getEmail(),player.getEmail(),GzTransaction.BANKERTURNOVER,bankerTurnover,now,source,player.getAccount().getTransactionMatchId());
			home.storeTransaction(transaction);
		}
		
		GzBaseUser member = player;
		while (true)
		{
			GzAccount account = member.getAccount();
			account.setTotalPlayer(account.getTotalPlayer()+turnover);
			account.setTotalBanker(account.getTotalBanker()+bankerTurnover);
			home.updateDistributions(account);
			if (member == topAgent)
				break;
			member = member.getParent();
		}
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
		
		double playerCoeff = account.getDistributePlayer()/account.getTotalPlayer();
		double bankerCoeff = account.getDistributeBanker()/account.getTotalBanker();
		
		for (GzBaseUser bu : topAgent.getMembers())								// SMA
		{
			if (account.getDistributePlayer()>0)
				createRoyaltyForPlayer(topAgent,bu,now,null,playerCoeff);
			if (account.getDistributeBanker()>0)
				createRoyaltyForBanker(topAgent,bu,now,null,bankerCoeff);
		}
		
		home.updateTransactionInvoiceIds(account.getTransactionMatchId());
	}
	
	private void createRoyaltyForPlayer(GzBaseUser parent,GzBaseUser baseUser,Date now, GzInvoice parentInvoice, double playerCoeff) throws GzPersistenceException
	{	
		GzAccount account = baseUser.getAccount();
		double royalty = account.getPlayerRoyalty() * playerCoeff;
		double netAmount = (account.getTotalPlayer() * royalty) /100.0;
		long parentInvoiceId = -1;
		if (parentInvoice != null)
			parentInvoiceId = parentInvoice.getId();
			
		GzInvoice invoice = new GzInvoice(parent.getEmail(),baseUser.getEmail(),GzTransaction.PLAYERTURNOVER,account.getTotalPlayer(),
										royalty,netAmount,now,parentInvoiceId,account.getTransactionMatchId());
		invoice = home.storeInvoice(invoice);
		home.getMembersForBaseUser(baseUser);
		for (GzBaseUser bu : baseUser.getMembers())
		{
			createRoyaltyForPlayer(baseUser,bu,now,invoice,playerCoeff);
		}
	}
	
	private void createRoyaltyForBanker(GzBaseUser parent,GzBaseUser baseUser,Date now, GzInvoice parentInvoice, double bankerCoeff) throws GzPersistenceException
	{
		GzAccount account = baseUser.getAccount();
		double royalty = account.getBankerRoyalty() * bankerCoeff;
		double netAmount = (account.getTotalBanker() * royalty) / 100.0;
		long parentInvoiceId = -1;
		if (parentInvoice != null)
			parentInvoiceId = parentInvoice.getId();
	
		GzInvoice invoice = new GzInvoice(parent.getEmail(),baseUser.getEmail(),GzTransaction.BANKERTURNOVER,account.getTotalBanker(),
							royalty,netAmount,now,parentInvoiceId,account.getTransactionMatchId());
		invoice = home.storeInvoice(invoice);
		home.getMembersForBaseUser(baseUser);
		for (GzBaseUser bu : baseUser.getMembers())
		{
			createRoyaltyForBanker(baseUser,bu,now,invoice,bankerCoeff);
		}
	}
	/*
	private GzInvoice storeOrUpdateInvoice(GzBaseUser payer,GzBaseUser payee,double amount,double commission,
			double netAmount,Date now,GzInvoice subInvoice) throws GzPersistenceException
	{
		GzInvoice invoice = home.getOpenInvoice(payer.getEmail(),payee.getEmail());
		if (invoice == null)
		{
			GregorianCalendar gc = new GregorianCalendar();
			gc.add(Calendar.HOUR,payer.getAccount().getPaymentDays()*24);
			invoice = new GzInvoice(payer.getEmail(),payee.getEmail(),amount,commission,netAmount,now,gc.getTime());
			home.storeInvoice(invoice);
		}
		else
		{
			double useAmount = amount;
			home.updateInvoice(useAmount, commission, netAmount,invoice.getId());
		}
		if (subInvoice != null)
			home.updateSubInvoice(subInvoice,invoice);
		return invoice;
	}
	*/
	@SuppressWarnings("unused")
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
