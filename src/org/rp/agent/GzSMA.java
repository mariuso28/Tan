package org.rp.agent;

import java.io.Serializable;
import java.util.List;

public class GzSMA extends GzMA  implements Serializable
{
	private static final long serialVersionUID = -821725843678548438L;
	private List<GzMA> mas;
	
	
	public void setMas(List<GzMA> mas) {
		this.mas = mas;
	}
	public List<GzMA> getMas() {
		return mas;
	}

}
