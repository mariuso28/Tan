package org.rp.agent;

import java.io.Serializable;
import java.util.List;

public class GzComp extends GzZMA implements Serializable
{
	private static final long serialVersionUID = 9094715882696811299L;
	private List<GzZMA> zmas;

	public void setZmas(List<GzZMA> zmas) {
		this.zmas = zmas;
	}

	public List<GzZMA> getZmas() {
		return zmas;
	}
}
