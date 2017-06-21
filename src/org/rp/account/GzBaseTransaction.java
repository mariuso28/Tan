package org.rp.account;

import java.io.Serializable;
import java.util.Date;

public class GzBaseTransaction implements Serializable{

	private static final long serialVersionUID = 8437361964356185435L;
	private long id;
	private String payer;
	private String payee;
	private Character type;
	protected double amount;
	private Date timestamp;
	
	public GzBaseTransaction()
	{
	}
	
	public GzBaseTransaction(String payer,String payee,double amount,Date timestamp)
	{
		setPayer(payer);
		setPayee(payee);
		setAmount(amount);
		setTimestamp(timestamp);
	}
	
	public GzBaseTransaction(String payer,String payee,Character type,double amount,Date timestamp)
	{
		this(payer,payee,amount,timestamp);
		setType(type);
		setTimestamp(timestamp);
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getPayer() {
		return payer;
	}
	public void setPayer(String payer) {
		this.payer = payer;
	}
	public String getPayee() {
		return payee;
	}
	public void setPayee(String payee) {
		this.payee = payee;
	}
	public Character getType() {
		return type;
	}
	public void setType(Character type) {
		this.type = type;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public Date getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}
	
	
	
}
