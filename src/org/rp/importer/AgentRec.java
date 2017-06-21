package org.rp.importer;

import java.util.Date;

import org.rp.agent.GzAgent;

public class AgentRec {

	private String contact;
	private String suffix;
	private Date date;
	private GzAgent agent;
	
	public AgentRec(String contact, Date date) throws CsvImporterException
	{
		setContact(contact);
		setDate(date);
		int pos = contact.indexOf("@");
		if (pos < 1)
			throw new CsvImporterException("Invalid agent contact : " + contact);
		setSuffix(contact.substring(pos));
	}

	public String getContact() {
		return contact;
	}

	public void setContact(String contact) {
		this.contact = contact;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSuffix() {
		return suffix;
	}

	public void setSuffix(String suffix) {
		this.suffix = suffix;
	}

	public GzAgent getAgent() {
		return agent;
	}

	public void setAgent(GzAgent agent) {
		this.agent = agent;
	}
	
	
}
