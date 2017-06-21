package org.rp.baseuser.persistence;

import java.util.List;
import java.util.UUID;

import org.rp.account.persistence.GzAccountDao;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.persistence.GzPersistenceException;
import org.springframework.web.multipart.MultipartFile;

public interface GzBaseUserDao extends GzAccountDao{

	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException;
	public GzBaseUser getBaseUserByEmail(String email, @SuppressWarnings("rawtypes") Class clazz) throws GzPersistenceException;
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException;
	public List<String> getMemberCodes(GzBaseUser baseUser) throws GzPersistenceException;
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException;
	public Double getDownStreamCreditAsPlayer(GzBaseUser user, GzBaseUser parent);
	public Double getDownStreamCreditAsBanker(GzBaseUser user, GzBaseUser parent);
	public void getDownstreamForParent(GzBaseUser parent);
	public void setAsSystemMember(GzBaseUser user) throws GzPersistenceException;
	public void storeImage(String email,MultipartFile data, String contentType) throws GzPersistenceException;
	public byte[] getImage(final String email) throws GzPersistenceException;
	public String getEmailForId(UUID id) throws GzPersistenceException;
	public void setDefaultPasswordForAll(String encoded);
	public void updateLeafInstance(GzBaseUser bu);

}
