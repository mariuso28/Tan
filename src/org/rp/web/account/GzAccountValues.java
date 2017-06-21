package org.rp.web.account;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.apache.log4j.Logger;
import org.rp.web.GzFormValidationException;
import org.rp.account.GzAccount;


public class GzAccountValues 
{
	private static final Logger log = Logger.getLogger(GzAccountValues.class);
	private String playerRoyalty;
	private String bankerRoyalty;
	
	public GzAccountValues()
	{
	}
	
	public GzAccountValues(GzAccount account)
	{
		NumberFormat formatter = new DecimalFormat("#0.00");     
		playerRoyalty = formatter.format(account.getPlayerRoyalty());
		bankerRoyalty = formatter.format(account.getBankerRoyalty());
	}
	
	public GzAccount createAccount(String pField,String bField) throws GzFormValidationException
	{
		log.info("Creating account from : " + this.toString());
		String invalidFields = "";
		int cnt = 0;
		GzAccount acc = new GzAccount();
		double pr=0;
		try
		{
			pr=Double.parseDouble(playerRoyalty);
		}
		catch (NumberFormatException e)
		{
			invalidFields +=pField+",";
			cnt++;
		}
		double br=0;
		try
		{
			br = Double.parseDouble(bankerRoyalty);
		}
		catch (NumberFormatException e)
		{
			invalidFields +=bField+",";
			cnt++;
		}
		if (cnt>0)
		{
			invalidFields = invalidFields.substring(0,invalidFields.length()-1);
			if (cnt == 1)
				throw new GzFormValidationException("Invalid format for field: " + invalidFields);
			else
				throw new GzFormValidationException("Invalid format for fields: " + invalidFields);
		}
		
		acc.setPlayerRoyalty(pr);
		acc.setBankerRoyalty(br);
		return acc;
	}
	
	public void populateAccount(GzAccount account) {
		account.setPlayerRoyalty(Double.parseDouble(playerRoyalty));
		account.setBankerRoyalty( Double.parseDouble(bankerRoyalty));
	}

	public String getPlayerRoyalty() {
		return playerRoyalty;
	}

	public void setPlayerRoyalty(String playerRoyalty) {
		this.playerRoyalty = playerRoyalty;
	}

	public String getBankerRoyalty() {
		return bankerRoyalty;
	}

	public void setBankerRoyalty(String bankerRoyalty) {
		this.bankerRoyalty = bankerRoyalty;
	}

	
 }
