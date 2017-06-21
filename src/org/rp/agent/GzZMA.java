package org.rp.agent;

import java.io.Serializable;
import java.util.List;

public class GzZMA extends GzSMA  implements Serializable{

	private static final long serialVersionUID = -7312928590551715604L;
	private List<GzSMA> smas;
	
	public void setSmas(List<GzSMA> smas) {
		this.smas = smas;
	}
	public List<GzSMA> getSmas() {
		return smas;
	} 
}
