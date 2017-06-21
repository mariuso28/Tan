package org.rp.web.account;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.rp.account.GzInvoice;
import org.rp.account.GzTransaction;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;

public class TransactionListForm 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TransactionListForm.class);
	
	private GzInvoice invoice;
	private String code;
	private GzRole role;
	private String email;
	private List<GzTransaction> displayList = new ArrayList<GzTransaction>();
	
	public TransactionListForm(GzBaseUser currUser,GzInvoice invoice,GzHome gzHome) throws GzPersistenceException
	{
		setInvoice(invoice);
		setRole(currUser.getRole());
		setCode(currUser.getCode());
		setEmail(currUser.getEmail());
		List<GzTransaction> trans = gzHome.getTransactionsForInvoice(invoice);
		for (GzTransaction tran : trans)
		{
			displayList.add(tran);
		}
	}
	
	public void setInvoice(GzInvoice invoice) {
		this.invoice = invoice;
	}

	public GzInvoice getInvoice() {
		return invoice;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	public void setRole(GzRole role) {
		this.role = role;
	}

	public GzRole getRole() {
		return role;
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<GzTransaction> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(List<GzTransaction> displayList) {
		this.displayList = displayList;
	}


}
