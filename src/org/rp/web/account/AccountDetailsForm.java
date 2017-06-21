package org.rp.web.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.account.GzAccount;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.util.StackDump;
import org.rp.web.GzFormValidationException;

public class AccountDetailsForm{
	
	private static final Logger log = Logger.getLogger(AccountDetailsForm.class);
	private AccountCommand command;
	private GzAccount account;
	private String message;
	private String info;
	private boolean modify;
	private List<GzInvoice> outstandingInvoices;
	private List<String> payFlags;
	private List<Boolean> overDueFlags;
	private double outstandingInvoicesTotal;
	private GzBaseUser payee;
	private GzBaseUser payer;
	private GzAccountValues prevAccount;
	
	public AccountDetailsForm()
	{
		setModify(false);
	}
	
	private AccountDetailsForm(GzBaseUser payer,GzBaseUser payee)
	{
		this();
		setPayer(payer);
		setPayee(payee);
		setAccount(payer.getAccount());
		setPrevAccount(new GzAccountValues(payer.getAccount()));
	}
	
	public AccountDetailsForm(GzHome gzHome,GzBaseUser payer,GzBaseUser payee)
	{
		this(payer,payee);
		try {
			setOutstandingInvoices(gzHome.getOutstandingInvoices(payer,payee));
		} catch (GzPersistenceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		payFlags = new ArrayList<String>();
		overDueFlags = new ArrayList<Boolean>();
		for (@SuppressWarnings("unused") GzInvoice invoice : outstandingInvoices)
		{
			payFlags.add("off");
		}
	}
	
	public void setPayFlagsOn(Boolean currChecked) {
		payFlags = new ArrayList<String>();
		for (@SuppressWarnings("unused") GzInvoice invoice : outstandingInvoices)
		{
			if (currChecked)
				payFlags.add("on");
			else
				payFlags.add("off");
		}
	}
	
	public AccountDetailsForm(GzHome gzHome,GzBaseUser user,GzBaseUser parent,AccountDetailsForm lastForm)
	{
		this(gzHome,user,parent);
		setPrevAccount(lastForm.getCommand().getNewAccount());
	}

	public void setCommand(AccountCommand command) {
		this.command = command;
	}

	public AccountCommand getCommand() {
		return command;
	}


	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}
	
	public void setModify(boolean modify) {
		this.modify = modify;
	}

	public boolean isModify() {
		return modify;
	}

	public void validate(GzServices gzServices,GzBaseUser user,GzBaseUser parent) throws GzFormValidationException
	{
		GzHome gzHome = gzServices.getGzHome();
		
		String pField = "Player Royalty";
		String bField = "Banker Royalty";
		GzAccount account = getCommand().getNewAccount().createAccount(pField,bField);
		
		if (user.getRole().equals(GzRole.ROLE_COMP))
		{
			pField = "Default Player Distribution";
			bField = "Default Banker Distribution";
		}
		
		if (account.getPlayerRoyalty() < 0.0)
			throw new GzFormValidationException(pField + " cannot be less than 0%");
		if (account.getBankerRoyalty() < 0.0)
			throw new GzFormValidationException(bField + " cannot be less than 0%");
		if (account.getPlayerRoyalty() > 100.0)
			throw new GzFormValidationException(pField + " cannot exceed 100%");
		if (account.getBankerRoyalty() > 100.0)
			throw new GzFormValidationException(bField + " cannot exceed 100%");
		
		if (parent.getRole().equals(GzRole.ROLE_ADMIN) || parent.getRole().equals(GzRole.ROLE_COMP))
		{	
			return;
		}
		
		if (account.getPlayerRoyalty()>parent.getAccount().getPlayerRoyalty())
			throw new GzFormValidationException("Maximum Player Royalty cannot be exceeded");
		
		if (account.getBankerRoyalty()>parent.getAccount().getBankerRoyalty())
			throw new GzFormValidationException("Maximum Banker Royalty cannot be exceeded");
		checkDownstreamRoyaltys(gzHome,user,account);
	}

	private void checkDownstreamRoyaltys(GzHome gzHome,GzBaseUser user, GzAccount account) throws GzFormValidationException{
		try {
			double dwc = gzHome.getHigestDownstreamRoyalty('B',user.getCode());
			double dbc = gzHome.getHigestDownstreamRoyalty('P',user.getCode());
			String msg = "";
			if (dbc > account.getPlayerRoyalty())
				msg += "Player Royalty cannot be set smaller than downstream member's";
			if (dwc > account.getBankerRoyalty())
				msg += "Banker Royalty cannot be set smaller than downstream member's ";
			if (!msg.isEmpty())
				throw new GzFormValidationException(msg);
		} catch (GzPersistenceException e) {
			log.error(StackDump.toString(e));
		}
	}

	public void setOutstandingInvoices(List<GzInvoice> outstandingInvoices) {
		this.outstandingInvoices = outstandingInvoices;
		outstandingInvoicesTotal = 0.0;
		for (GzInvoice invoice : outstandingInvoices)
			if (invoice.getPayee().equals(payee.getEmail()))
				outstandingInvoicesTotal-=invoice.getNetAmount();
			else
				outstandingInvoicesTotal+=invoice.getNetAmount();
	}

	public List<GzInvoice> getOutstandingInvoices() {
		return outstandingInvoices;
	}

	public void setOutstandingInvoicesTotal(double outstandingInvoicesTotal) {
		this.outstandingInvoicesTotal = outstandingInvoicesTotal;
	}

	public double getOutstandingInvoicesTotal() {
		return outstandingInvoicesTotal;
	}

	public List<String> getPayFlags() {
		return payFlags;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setOverDueFlags(List<Boolean> overDueFlags) {
		this.overDueFlags = overDueFlags;
	}

	public List<Boolean> getOverDueFlags() {
		return overDueFlags;
	}
	
	public GzBaseUser getPayee() {
		return payee;
	}

	public void setPayee(GzBaseUser payee) {
		this.payee = payee;
	}

	public GzBaseUser getPayer() {
		return payer;
	}

	public void setPayer(GzBaseUser payer) {
		this.payer = payer;
	}
	
	public GzAccount getAccount() {
		return account;
	}

	public void setAccount(GzAccount account) {
		this.account = account;
	}

	public void setPayFlags(List<String> payFlags) {
		this.payFlags = payFlags;
	}

	public GzAccountValues getPrevAccount() {
		return prevAccount;
	}

	public void setPrevAccount(GzAccountValues prevAccount) {
		this.prevAccount = prevAccount;
	}
	
	
	
}
