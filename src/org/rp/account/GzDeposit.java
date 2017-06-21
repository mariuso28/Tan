package org.rp.account;

import java.util.Date;

public class GzDeposit extends GzWithdrawl {
	
	private static final long serialVersionUID = 2761719978870920506L;

	public GzDeposit()
	{
	}
	
	public GzDeposit(String payee,double amount,Date timestamp)
	{
		super("",amount,timestamp);
		setPayee(payee);
		setType(GzXaction.XTYPEDEPOSIT);
	}
}
