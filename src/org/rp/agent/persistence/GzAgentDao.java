package org.rp.agent.persistence;

import org.rp.agent.GzAgent;
import org.rp.home.persistence.GzPersistenceException;

public interface GzAgentDao{

	public void store(GzAgent agent) throws GzPersistenceException;
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException;
	public GzAgent getAgentByCode(String code) throws GzPersistenceException;
	
}
