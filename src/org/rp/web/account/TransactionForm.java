package org.rp.web.account;

import java.util.List;

import org.apache.log4j.Logger;
import org.rp.home.GzHome;
import org.rp.account.GzBaseTransaction;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;

public class TransactionForm 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(TransactionForm.class);
	private String contact;
	private String code;
	private GzRole role;
	private List<GzBaseTransaction> txList;
	private int currentPage;
	private int lastPage;
	
	public TransactionForm(String contact,GzRole role,String code)
	{
		setContact(contact);
		setRole(role);
		setCode(code);
	}
	
	public void createTxList(GzBaseUser user,GzHome dx4Home,List<GzBaseTransaction> txList,int currentPage,int lastPage)
	{
		setTxList(txList);
		setCurrentPage(currentPage);
		setLastPage(lastPage);
	}
	
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public GzRole getRole() {
		return role;
	}
	public void setRole(GzRole role) {
		this.role = role;
	}

	

	public List<GzBaseTransaction> getTxList() {
		return txList;
	}

	public void setTxList(List<GzBaseTransaction> txList) {
		this.txList = txList;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getLastPage() {
		return lastPage;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}

	

}
