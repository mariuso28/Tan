package org.rp.web.adm.importer;

import java.io.Serializable;

public class GzImportForm implements Serializable{
	
	private static final long serialVersionUID = -7739696837150078796L;
	private GzImportCommand command;
	private String errMsg;
	private String infoMsg;
	
	public GzImportForm()
	{
	}

	public GzImportCommand getCommand() {
		return command;
	}

	public void setCommand(GzImportCommand command) {
		this.command = command;
	}

	public String getErrMsg() {
		return errMsg;
	}

	public void setErrMsg(String errMsg) {
		this.errMsg = errMsg;
	}

	public String getInfoMsg() {
		return infoMsg;
	}

	public void setInfoMsg(String infoMsg) {
		this.infoMsg = infoMsg;
	}
}
