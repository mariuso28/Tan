package org.rp.web.account;

import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.rp.account.GzAccount;
import org.rp.web.GzFormValidationException;

public class GzAccountDistributionCommand {

	private String playerDistribution;
	private String bankerDistribution;
	
	public GzAccountDistributionCommand()
	{
	}
	
	public GzAccountDistributionCommand(GzAccount account)
	{
		NumberFormat formatter = new DecimalFormat("#0.00");     
		playerDistribution = formatter.format(account.getDistributePlayer());
		bankerDistribution = formatter.format(account.getDistributeBanker());
	}

	public void populateAccount(GzAccount account) throws GzFormValidationException
	{
		String invalidFields = "";
		int cnt = 0;
		double pr=0;
		try
		{
			pr=Double.parseDouble(playerDistribution);
		}
		catch (NumberFormatException e)
		{
			invalidFields +="pField,";
			cnt++;
		}
		double br=0;
		try
		{
			br = Double.parseDouble(bankerDistribution);
		}
		catch (NumberFormatException e)
		{
			invalidFields +="bField,";
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
		
		if (pr > account.getTotalPlayer())
			throw new GzFormValidationException("Player Distribution cannot exceed Total Player Turnover");
		if (br > account.getTotalPlayer())
			throw new GzFormValidationException("Banker Distribution cannot exceed Total Banker Turnover");
		account.setDistributePlayer(pr);
		account.setDistributeBanker(br);
	}

	public String getPlayerDistribution() {
		return playerDistribution;
	}

	public void setPlayerDistribution(String playerDistribution) {
		this.playerDistribution = playerDistribution;
	}

	public String getBankerDistribution() {
		return bankerDistribution;
	}

	public void setBankerDistribution(String bankerDistribution) {
		this.bankerDistribution = bankerDistribution;
	}
	
	
}
