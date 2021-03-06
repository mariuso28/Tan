package org.rp.account;

import java.util.Date;
import java.util.UUID;

public class GzInvoice extends GzDeposit {
	
	private static final long serialVersionUID = -4558471539417421128L;
	
	public static final char STATUSOPEN = 'O';
	public static final char STATUSCLOSED = 'C';
	public static final char STATUSSETTLED = 'S';
	
	private double royalty;
	private double netAmount;
	private long parentId;
	private Date paymentDate;
	private char status;
	private char invoiceType;
	private UUID matchId;
	
	public GzInvoice()
	{
	}

	public GzInvoice(String payer,String payee,char invoiceType,double amount,double royalty,double netAmount,Date timestamp,long parentId,UUID matchId)
	{
		setPayer(payer);
		setPayee(payee);
		setInvoiceType(invoiceType);
		setType(GzXaction.XTYPEINVOICE);
		setAmount(amount);
		setTimestamp(timestamp);
		setRoyalty(royalty);
		setNetAmount(netAmount);
		setParentId(parentId);
		setMatchId(matchId);
	}

	public double getNetAmount() {
		return netAmount;
	}

	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	public long getParentId() {
		return parentId;
	}

	public void setParentId(long parentId) {
		this.parentId = parentId;
	}

	public Date getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(Date paymentDate) {
		this.paymentDate = paymentDate;
	}

	public char getStatus() {
		return status;
	}

	public void setStatus(char status) {
		this.status = status;
	}
	
	public double getRoyalty() {
		return royalty;
	}

	public void setRoyalty(double royalty) {
		this.royalty = royalty;
	}

	public void update(double amount2, double royalty2, double netAmount2,double stake2,char winstake) {
		amount += amount2;
		royalty += royalty2;
		netAmount += netAmount2;
	}

	public char getInvoiceType() {
		return invoiceType;
	}

	public void setInvoiceType(char invoiceType) {
		this.invoiceType = invoiceType;
	}

	public UUID getMatchId() {
		return matchId;
	}

	public void setMatchId(UUID matchId) {
		this.matchId = matchId;
	}

	
}
