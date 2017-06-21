package org.rp.agent;

import java.io.Serializable;
import java.util.List;

import org.rp.baseuser.GzBaseUser;

public class GzAgent extends GzBaseUser implements Serializable
{
	private static final long serialVersionUID = 1832416920445925711L;
	private List<GzBaseUser> players;
	
	public GzAgent()
	{
	}

	public List<GzBaseUser> getPlayers() {
		return players;
	}

	public void setPlayers(List<GzBaseUser> players) {
		this.players = players;
	}
	
}
