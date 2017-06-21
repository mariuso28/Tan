package org.rp.web.account;

import java.util.List;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.home.persistence.GzPersistenceException;
import org.rp.account.GzInvoice;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;

public class InvoiceListForm 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(InvoiceListForm.class);
	
	private GzInvoice invoice;
	private List<GzInvoice> displayList;
	private String code;
	private GzRole role;
	private String infoMsg;
	private String errMsg;
	
	public InvoiceListForm(GzBaseUser currAccountUser,GzInvoice invoice,GzHome GzHome) throws GzPersistenceException
	{
		setInvoice(invoice);
		setRole(currAccountUser.getRole());
		setCode(currAccountUser.getCode());
		displayList = GzHome.getInvoicesForInvoice(invoice);
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

	public List<GzInvoice> getDisplayList() {
		return displayList;
	}

	public void setDisplayList(List<GzInvoice> displayList) {
		this.displayList = displayList;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}
}
