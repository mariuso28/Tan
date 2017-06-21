package org.rp.web.account;

import java.util.ArrayList;
import java.util.List;

public class AccountCommand{

	private GzAccountValues newAccount;
	private List<Long> invoiceIds;
	private List<String> payFlags;

	public AccountCommand()
	{
		invoiceIds = new ArrayList<Long>();
		payFlags = new ArrayList<String>();;
	}
	
	public GzAccountValues getNewAccount() {
		return newAccount;
	}

	public void setNewAccount(GzAccountValues newAccount) {
		this.newAccount = newAccount;
	}
	
	public void setPayFlags(List<String> payFlags) {
		this.payFlags = payFlags;
	}

	public List<String> getPayFlags() {
		return payFlags;
	}

	public void setInvoiceIds(List<Long> invoiceIds) {
		this.invoiceIds = invoiceIds;
	}

	public List<Long> getInvoiceIds() {
		return invoiceIds;
	}

	
}