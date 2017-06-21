package org.rp.admin.persistence;

import org.rp.admin.GzAdmin;
import org.rp.home.persistence.GzPersistenceException;

public interface GzAdminDao{

	public void store(GzAdmin agent) throws GzPersistenceException;
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException;
	public GzAdmin getAdminByCode(String code) throws GzPersistenceException;
	
}
