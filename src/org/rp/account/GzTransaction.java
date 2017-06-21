package org.rp.account;

import java.util.Date;

public class GzTransaction extends GzBaseTransaction {
	
	private static final long serialVersionUID = 659183036584351555L;
	private String source;
	private long invoiceId;
	public final static char PLAYERTURNOVER = 'P';
	public final static char BANKERTURNOVER = 'B';
	
	public GzTransaction()
	{
	}
	
	public GzTransaction(String payer,String payee,Character type,double amount,Date timestamp,String source)
	{
		super(payer,payee,type,amount,timestamp);
		setType(type);
		setSource(source);
	}
	
	public long getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(long invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}

	
}
