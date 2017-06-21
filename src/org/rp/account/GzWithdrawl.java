package org.rp.account;

import java.util.Date;

public class GzWithdrawl extends GzBaseTransaction {
	private static final long serialVersionUID = 3319681810434749912L;
	
	public GzWithdrawl()
	{
	}
	
	public GzWithdrawl(String payer,double amount,Date timestamp)
	{
		super(payer,"",GzXaction.XTYPEWITHDRAWL,amount,timestamp);
	}
	
}
