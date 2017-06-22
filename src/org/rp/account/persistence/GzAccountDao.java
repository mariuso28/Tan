package org.rp.account.persistence;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.rp.account.GzAccount;
import org.rp.account.GzInvoice;
import org.rp.account.GzRollup;
import org.rp.account.GzTransaction;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.persistence.GzPersistenceException;

public interface GzAccountDao {

	public void updateAccount(GzAccount account) throws GzPersistenceException;
	public void updateDistributions(GzAccount account) throws GzPersistenceException;
	public void updateAccountBalance(GzAccount account,double amount) throws GzPersistenceException;
	public void storeAccount(UUID baseUserId) throws GzPersistenceException;
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException;
	public void storeTransaction(GzTransaction trans) throws GzPersistenceException;
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException;
	public Long getOutstandingTransactionCount();
	public GzRollup getRollupForUser(String userId,String code,GzRole role);
	public List<GzRollup> getMemberRollups(GzBaseUser currUser);
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException;
	public Long getXactionCount(GzBaseUser currUser);
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException;
	public GzInvoice getOpenInvoice(String payer, String payee) throws GzPersistenceException;
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void updateInvoice(double amount, double Royalty, double netAmount, long id) throws GzPersistenceException;
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException;
	public GzInvoice storeInvoice(GzInvoice invoice) throws GzPersistenceException;
	public void updatePayment(GzInvoice invoice) throws GzPersistenceException;
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException;
	public void closeOpenInvoices() throws GzPersistenceException;
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException;
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException;
	public double getHigestDownstreamRoyalty(char type, String code) throws GzPersistenceException;
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException;
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException;
	public void clearTurnoversAndDistributions() throws GzPersistenceException;
	public void updateTransactionInvoiceIds(UUID transactionMatchId) throws GzPersistenceException;
	
	
}
