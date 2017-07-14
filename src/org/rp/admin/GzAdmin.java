package org.rp.admin;

import java.io.Serializable;
import java.util.List;

import org.rp.agent.GzSMA;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;

public class GzAdmin extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = -2070754452251455632L;
	private List<GzSMA> smas;
	
	public GzAdmin()
	{
	}
	
	public GzAdmin(String email) {
		super(email);
		setCode(getDefaultCode());						// has to be x to match with role class
		setParentCode("");
		setRole(GzRole.ROLE_ADMIN);
	}

	public static String getDefaultCode() {
		return "x0";
	}

	public List<GzSMA> getSmas() {
		return smas;
	}

	public void setSmas(List<GzSMA> smas) {
		this.smas = smas;
	}

}
