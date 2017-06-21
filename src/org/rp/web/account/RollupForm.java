package org.rp.web.account;

import java.util.List;

import org.rp.home.persistence.GzPersistenceException;
import org.rp.services.GzServices;
import org.rp.account.GzInvoice;
import org.rp.account.GzRollup;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;


public class RollupForm {

	private String contact;
	private String email;
	private String parentContact;
	private String parentRole;
	private String parentEmail;
	private GzRole role;
	private GzRollup rollup;
	private List<GzRollup> memberRollups;
	private List<GzInvoice> outstandingInvoices;
	private double outstandingInvoicesTotal;
	private String message;
	private String info;

	public RollupForm(GzServices gzServices,GzBaseUser currUser) throws GzPersistenceException {
		
		setEmail(currUser.getEmail());
		GzRollup rollup = gzServices.getGzHome().getRollupForUser(currUser.getEmail(),currUser.getCode(),currUser.getRole());
		
		GzBaseUser parent = gzServices.getGzHome().getParentForUser(currUser);
		setParentRole(parent.getRole().getDesc());
		setParentContact(parent.getContact());
		setParentEmail(parent.getEmail());
		setRollup(rollup);
		setMemberRollups(gzServices.getGzHome().getMemberRollups(currUser));
		
		setContact(currUser.getContact());
		setRole(currUser.getRole());
		
		setOutstandingInvoices(gzServices.getGzHome().getOutstandingInvoices(parent,currUser));
		totalOutstandingInvoices();
	}

	public void totalOutstandingInvoices() {
		outstandingInvoicesTotal = 0.0;
		for (GzInvoice invoice : outstandingInvoices)
		{
			if (invoice.getPayee().equals(email))
				outstandingInvoicesTotal-=invoice.getNetAmount();
			else
				outstandingInvoicesTotal+=invoice.getNetAmount();
		}
	}
	
	public void setRollup(GzRollup rollup) {
		this.rollup = rollup;
	}

	public GzRollup getRollup() {
		return rollup;
	}

	public void setMemberRollups(List<GzRollup> memberRollups) {
		this.memberRollups = memberRollups;
	}

	public List<GzRollup> getMemberRollups() {
		return memberRollups;
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public void setRole(GzRole role) {
		this.role = role;
	}

	public GzRole getRole() {
		return role;
	}

	public List<GzInvoice> getOutstandingInvoices() {
		return outstandingInvoices;
	}
	
	public void setOutstandingInvoices(List<GzInvoice> outstandingInvoices) {
		this.outstandingInvoices = outstandingInvoices;
	}

	public double getOutstandingInvoicesTotal() {
		return outstandingInvoicesTotal;
	}

	public void setOutstandingInvoicesTotal(double outstandingInvoicesTotal) {
		this.outstandingInvoicesTotal = outstandingInvoicesTotal;
	}

	public String getParentContact() {
		return parentContact;
	}

	public void setParentContact(String parentContact) {
		this.parentContact = parentContact;
	}

	public String getParentRole() {
		return parentRole;
	}

	public void setParentRole(String parentRole) {
		this.parentRole = parentRole;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public void setParentEmail(String parentEmail) {
		this.parentEmail = parentEmail;
	}

	public String getParentEmail() {
		return parentEmail;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	

}
