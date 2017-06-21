package org.rp.admin;

import java.io.Serializable;
import java.util.List;

import org.rp.agent.GzComp;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;

public class GzAdmin extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = -2070754452251455632L;
	private List<GzComp> comps;
	
	public GzAdmin()
	{
	}
	
	public GzAdmin(String email) {
		super(email);
		setCode(getDefaultCode());						// has to be x to match with role class
		setParentCode("");
		setRole(GzRole.ROLE_ADMIN);
	}
	
	public void setComps(List<GzComp> comps) {
		this.comps = comps;
	}

	public List<GzComp> getComps() {
		return comps;
	}

	public static String getDefaultCode() {
		return "x0";
	}

}
