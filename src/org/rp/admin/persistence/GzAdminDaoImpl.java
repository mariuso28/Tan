package org.rp.admin.persistence;

import org.apache.log4j.Logger;
import org.rp.admin.GzAdmin;
import org.rp.baseuser.GzRole;
import org.rp.baseuser.persistence.GzBaseUserDaoImpl;
import org.rp.home.persistence.GzPersistenceException;


public class GzAdminDaoImpl extends GzBaseUserDaoImpl implements GzAdminDao 
{
	@SuppressWarnings("unused")
	private static final Logger log = Logger.getLogger(GzAdminDaoImpl.class);

	@Override
	public void store(GzAdmin agent) throws GzPersistenceException
	{
		super.storeBaseUser(agent);
	}
		
	@Override
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException {
		
		String code = super.getCodeForEmail(email);
		@SuppressWarnings("rawtypes")
		Class clazz = GzRole.getRoleClassForCode(code);
		GzAdmin agent = (GzAdmin) super.getBaseUserByEmail(email,clazz);
		if (agent == null)
			return null;
		
		super.getDownstreamForParent(agent);
		return agent;
	}

	@Override
	public GzAdmin getAdminByCode(String code) throws GzPersistenceException {
		
		GzAdmin agent = (GzAdmin) getBaseUserByCode(code);
		return agent;
	}
}
