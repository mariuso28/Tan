package org.rp.web.account;

import org.apache.log4j.Logger;
import org.rp.account.GzAccount;

public class AccountDistributionForm{
	
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(AccountDistributionForm.class);
	private GzAccountDistributionCommand command;
	private GzAccountDistributionCommand prevCommand;
	private String message;
	private String info;
	
	public AccountDistributionForm()
	{
	}
	
	public AccountDistributionForm(GzAccount account)
	{
		prevCommand = new GzAccountDistributionCommand(account);
	}

	public GzAccountDistributionCommand getCommand() {
		return command;
	}

	public void setCommand(GzAccountDistributionCommand command) {
		this.command = command;
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

	public GzAccountDistributionCommand getPrevCommand() {
		return prevCommand;
	}

	public void setPrevCommand(GzAccountDistributionCommand prevCommand) {
		this.prevCommand = prevCommand;
	}
	
}
