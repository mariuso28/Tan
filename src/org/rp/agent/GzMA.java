package org.rp.agent;

import java.io.Serializable;
import java.util.List;

public class GzMA extends GzAgent implements Serializable
{
	private static final long serialVersionUID = -2903769616665053902L;
	private List<GzAgent> agents;
	
	
	public void setAgents(List<GzAgent> agents) {
		this.agents = agents;
	}
	public List<GzAgent> getAgents() {
		return agents;
	}
}
