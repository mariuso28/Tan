package org.rp.home.persistence;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.log4j.Logger;
import org.rp.account.GzAccount;
import org.rp.account.GzInvoice;
import org.rp.account.GzRollup;
import org.rp.account.GzTransaction;
import org.rp.account.persistence.GzAccountDao;
import org.rp.account.persistence.GzTransactionRowMapperPaginated;
import org.rp.account.persistence.GzXactionRowMapperPaginated;
import org.rp.admin.GzAdmin;
import org.rp.admin.persistence.GzAdminDao;
import org.rp.agent.GzAgent;
import org.rp.agent.persistence.GzAgentDao;
import org.rp.baseuser.GzBaseUser;
import org.rp.baseuser.GzRole;
import org.rp.baseuser.persistence.GzBaseUserDao;
import org.rp.baseuser.persistence.GzBaseUserStub;
import org.rp.home.GzHome;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.multipart.MultipartFile;

public class GzHomeDaoImpl implements GzHome {
	protected static Logger log = Logger.getLogger(GzHomeDaoImpl.class);

	private GzBaseUserDao gzBaseUserDao;
	private GzAdminDao gzAdminDao;
	private GzAgentDao gzAgentDao;
	private GzAccountDao gzAccountDao;
	private String closeInvoiceStartAt;
	private String closeInvoiceAfterMins;
	private int closeInvoiceAfter;
	
	private JdbcTemplate jdbcTemplate;
	
	@Override
	public JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void overrideDataSourceUrl(String url)
	{
		BasicDataSource dataSource = (BasicDataSource) jdbcTemplate.getDataSource();
        dataSource.setUrl(url);
	}
	
	public GzAccountDao getGzAccountDao() {
		return gzAccountDao;
	}

	public void setGzAccountDao(GzAccountDao gzAccountDao) {
		this.gzAccountDao = gzAccountDao;
	}

	
	@Override
	public void getMembersForBaseUser(GzBaseUser baseUser) throws GzPersistenceException
	{
		List<GzBaseUser> members = new ArrayList<GzBaseUser>();
		List<String> codes = gzBaseUserDao.getMemberCodes(baseUser);
		for (String code : codes)
		{
			GzRole role = GzRole.getRoleForCode(code);
			if (role.isAgentRole())
				members.add(gzAgentDao.getAgentByCode(code));
			else
				members.add(getBaseUserByCode(code));
		}
		baseUser.setMembers(members);
	}
	
	@Override
	public void storeBaseUser(GzBaseUser baseUser) throws GzPersistenceException
	{
		gzBaseUserDao.storeBaseUser(baseUser);
	}
	
	@Override
	public GzBaseUser getBaseUserByEmail(String email) throws GzPersistenceException
	{
		return gzBaseUserDao.getBaseUserByEmail(email, GzBaseUser.class);
	}

	
	public GzAgentDao getGzAgentDao() {
		return gzAgentDao;
	}

	public void setGzAgentDao(GzAgentDao gzAgentDao) {
		this.gzAgentDao = gzAgentDao;
	}

	

	public GzBaseUserDao getGzBaseUserDao() {
		return gzBaseUserDao;
	}

	public void setGzBaseUserDao(GzBaseUserDao gzBaseUserDao) {
		this.gzBaseUserDao = gzBaseUserDao;
	}

	
	@Override
	public GzAgent getAgentByCode(String code) throws GzPersistenceException {
		return gzAgentDao.getAgentByCode(code);
	}

	@Override
	public void storeTransaction(GzTransaction transaction) throws GzPersistenceException {
		gzAccountDao.storeTransaction(transaction);
	}

	@Override
	public void storeAgent(GzAgent agent) throws GzPersistenceException {
		gzAgentDao.store(agent);
	}

	@Override
	public GzAgent getAgentByEmail(String email) throws GzPersistenceException {
		return gzAgentDao.getAgentByEmail(email);
	}

	@Override
	public void updateBaseUserProfile(GzBaseUser baseUser) throws GzPersistenceException {
		gzBaseUserDao.updateBaseUserProfile(baseUser);
	}

	@Override
	public void updateTransaction(GzTransaction trans) throws GzPersistenceException {
		gzAccountDao.updateTransaction(trans);
	}

	@Override
	public GzTransactionRowMapperPaginated getGzTransactionRowMapperPaginated(int count) {
		
		return new GzTransactionRowMapperPaginated(gzAccountDao,count);
	}

	@Override
	public GzXactionRowMapperPaginated getGzInvoiceRowMapperPaginated(GzBaseUser user,int count) {
		
		return new GzXactionRowMapperPaginated(user,gzAccountDao,count);
	}
	

	@Override
	public GzRollup getRollupForUser(String userId,String code,GzRole role) {
		return gzAccountDao.getRollupForUser(userId,code,role);
	}

	@Override
	public GzBaseUser getParentForUser(GzBaseUser currUser) throws GzPersistenceException{
		currUser.setParent(gzBaseUserDao.getBaseUserByCode(currUser.getParentCode()));
		return currUser.getParent();
	}

	@Override
	public List<GzRollup> getMemberRollups(GzBaseUser currUser) {
		
		return gzAccountDao.getMemberRollups(currUser);
	}

	@Override
	public List<GzInvoice> getOutstandingInvoices(GzBaseUser payer, GzBaseUser payee) throws GzPersistenceException{
	
		return gzAccountDao.getOutstandingInvoices(payer,payee);
	}

	@Override
	public void performWithdrawlDeposit(GzBaseUser currAccountUser, String dwType, double dwAmount) throws GzPersistenceException {
		gzAccountDao.performWithdrawlDeposit(currAccountUser,dwType,dwAmount);
	}

	@Override
	public GzBaseUser getBaseUserByCode(String code) throws GzPersistenceException {
		
		return gzBaseUserDao.getBaseUserByCode(code);
	}

	@Override
	public void updateEnabled(GzBaseUser currAccountUser) throws GzPersistenceException {
		gzBaseUserDao.updateBaseUserProfile(currAccountUser);
	}

	@Override
	public void getDownstreamForParent(GzBaseUser currUser) {
		gzBaseUserDao.getDownstreamForParent(currUser);
	}

	@Override
	public void reassignMemberRole(GzBaseUser baseUser, GzRole newRole)
	{
		gzBaseUserDao.reassignMemberRole(baseUser, newRole);
	}
	
	@Override
	public List<GzBaseUserStub> getUpstreamPossibleParents(GzRole role,String type,String term) throws GzPersistenceException
	{
		return gzBaseUserDao.getUpstreamPossibleParents(role,type,term);
	}
	
	@Override
	public GzInvoice getOpenInvoice(String payer, String payee) throws GzPersistenceException {
		return gzAccountDao.getOpenInvoice(payer,payee);
	}

	@Override
	public void updateInvoice(GzInvoice invoice) throws GzPersistenceException {
		gzAccountDao.updateInvoice(invoice);
	}
	
	@Override
	public void updateInvoice(double amount, double Royalty, double netAmount,  long id)
			throws GzPersistenceException {
		gzAccountDao.updateInvoice(amount, Royalty, netAmount, id);		
	}

	@Override
	public void updatePayment(GzInvoice invoice)
			throws GzPersistenceException {
		gzAccountDao.updatePayment(invoice);		
	}
	
	@Override
	public void updateSubInvoice(GzInvoice subInvoice, GzInvoice invoice) throws GzPersistenceException {
		gzAccountDao.updateSubInvoice(subInvoice,invoice);
	}

	@Override
	public GzInvoice storeInvoice(GzInvoice invoice) throws GzPersistenceException {
		return gzAccountDao.storeInvoice(invoice);
	}

	@Override
	public void changeInvoiceStatus(GzInvoice invoice,char status) throws GzPersistenceException {
		gzAccountDao.changeInvoiceStatus(invoice,status);
	}

	@Override
	public void closeOpenInvoices() throws GzPersistenceException {
		gzAccountDao.closeOpenInvoices();
	}

	@Override
	public String getCloseInvoiceAfterMins() {
		return closeInvoiceAfterMins;
	}

	@Override
	public void setCloseInvoiceAfterMins(String closeInvoiceAfterMins) {
		this.closeInvoiceAfterMins = closeInvoiceAfterMins;
	}

	@Override
	public int getCloseInvoiceAfter() {
		return closeInvoiceAfter;
	}
	
	@Override
	public void setCloseInvoiceAfter(int closeInvoiceAfter) {
		this.closeInvoiceAfter = closeInvoiceAfter;
	}

	@Override
	public GzInvoice getInvoiceForId(long invoiceNum) throws GzPersistenceException{
		return gzAccountDao.getInvoiceForId(invoiceNum);
	}

	@Override
	public List<GzInvoice> getInvoicesForInvoice(GzInvoice invoice) throws GzPersistenceException {
		return gzAccountDao.getInvoicesForInvoice(invoice);
	}

	@Override
	public List<GzTransaction> getTransactionsForInvoice(GzInvoice invoice) throws GzPersistenceException {
		return gzAccountDao.getTransactionsForInvoice(invoice);
	}

		@Override
	public double getHigestDownstreamRoyalty(char type, String code) throws GzPersistenceException {
		return gzAccountDao.getHigestDownstreamRoyalty(type, code);
	}

	@Override
	public String getEmailForId(UUID id) throws GzPersistenceException {
		return gzBaseUserDao.getEmailForId(id);
	}

	public void storeImage(String email, MultipartFile data, String contentType) throws GzPersistenceException
	{
		gzBaseUserDao.storeImage(email,data,contentType);
	}

	@Override
	public byte[] getImage(String email) throws GzPersistenceException {
		
		return gzBaseUserDao.getImage(email);
	}

	@Override
	public void updateAccount(GzAccount account) throws GzPersistenceException {
		gzAccountDao.updateAccount(account);
	}

	@Override
	public void updateDistributions(GzAccount account) throws GzPersistenceException {
		gzAccountDao.updateDistributions(account);
	}
	
	public void clearTurnoversAndDistributions() throws GzPersistenceException
	{
		gzAccountDao.clearTurnoversAndDistributions();
	}
	
	@Override
	public void updateAccountBalance(GzAccount account, double amount) throws GzPersistenceException {
		gzAccountDao.updateAccountBalance(account, amount);
	}

	@Override
	public GzAccount getAccount(GzBaseUser baseUser) throws GzPersistenceException {
		return gzAccountDao.getAccount(baseUser);
	}

	@Override
	public void setDefaultPasswordForAll(String encoded) {
		gzBaseUserDao.setDefaultPasswordForAll(encoded);
	}

	public String getCloseInvoiceStartAt() {
		return closeInvoiceStartAt;
	}

	public void setCloseInvoiceStartAt(String closeInvoiceStartAt) {
		this.closeInvoiceStartAt = closeInvoiceStartAt;
	}

	@Override
	public Map<UUID, Double> getOutstandingInvoiceAmounts(GzBaseUser user) throws GzPersistenceException {
		return gzAccountDao.getOutstandingInvoiceAmounts(user);
	}

	@Override
	public double getDownStreamAccountBalance(GzBaseUser user, GzBaseUser parent) throws GzPersistenceException{
		return parent.getAccount().getBalance() - gzAccountDao.getDownStreamAccountBalance(user, parent);
	}

	public void updateLeafInstance(GzBaseUser bu)
	{
		gzBaseUserDao.updateLeafInstance(bu);
	}

	@Override
	public List<String> getColumns(String table) throws GzPersistenceException{

		List<String> cols = new ArrayList<String>();
        try {
            // Gets the metadata of the database
        	Connection conn = getJdbcTemplate().getDataSource().getConnection();
            DatabaseMetaData dbmd = conn.getMetaData();

            ResultSet rs = dbmd.getColumns(null, null, table, null);
                while (rs.next()) {
                    String colummName = rs.getString("COLUMN_NAME");
                    cols.add(colummName);
            }
            conn.close();
        } catch (SQLException e) {
        	e.printStackTrace();
        	throw new GzPersistenceException(e.getMessage());
        }
        return cols;
    }

	@Override
	public GzAdmin getAdminByEmail(String email) throws GzPersistenceException {
		return gzAdminDao.getAdminByEmail(email);
	}

	public GzAdminDao getGzAdminDao() {
		return gzAdminDao;
	}

	public void setGzAdminDao(GzAdminDao gzAdminDao) {
		this.gzAdminDao = gzAdminDao;
	}

	@Override
	public void updateTransactionInvoiceIds(UUID transactionMatchId) throws GzPersistenceException {
		gzAccountDao.updateTransactionInvoiceIds(transactionMatchId);
	}

	@Override
	public List<GzBaseUser> getUpstreaMembers(GzRole role) {
		return gzBaseUserDao.getUpstreaMembers(role);
	}

	@Override
	public void updateBaseUserParentCode(String code, String parentCode) {
		gzBaseUserDao.updateBaseUserParentCode(code,parentCode);
	}

	@Override
	public List<GzBaseUserStub> search(String term,String type) {
	
		return gzBaseUserDao.search(term,type);
	}

	
}
