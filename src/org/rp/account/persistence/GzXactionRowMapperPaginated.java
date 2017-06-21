package org.rp.account.persistence;


import java.util.List;

import org.apache.log4j.Logger;
import org.rp.account.GzBaseTransaction;
import org.rp.baseuser.GzBaseUser;
import org.rp.home.persistence.PageRowCallbackHandler;
import org.rp.util.Util;



public class GzXactionRowMapperPaginated extends GzXactionRowMapper
{
	private static final Logger log = Logger.getLogger(GzXactionRowMapperPaginated.class);
	
	private Long count;
	private int currentPage;
	private int lastPage;
	private int pageSize;
	private GzAccountDao accountDao;
	private GzBaseUser user;
	
	public GzXactionRowMapperPaginated(GzBaseUser user,GzAccountDao accountDao, int pageSize )
	{
		setUser(user);
		this.accountDao = accountDao;
		count = accountDao.getXactionCount(user);
		currentPage = 0;
		this.pageSize = pageSize;
		lastPage = (int) (count/pageSize);
		if (count%pageSize>0)
			lastPage++;
		log.info("Created : " + this.toString());
	}
	
	public List<GzBaseTransaction> getNextPage()
	{
		currentPage++;
		return Util.toList(getPage());
	}

	public List<GzBaseTransaction> getPrevPage()
	{
		if (currentPage>0)
			currentPage--;
		return Util.toList(getPage());
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public List<GzBaseTransaction> getPage(int page)
	{
		currentPage=page;
		return Util.toList(getPage());
	}
	
	private Iterable<GzBaseTransaction> getPage()
	{
		long endIndex = currentPage * pageSize;
		long startIndex = endIndex - pageSize;
		endIndex--;
		if (endIndex > count)
			endIndex = count;
		
		log.info("Getting page : " + currentPage + " startIndex : " + startIndex + " endIndex: " + endIndex );
		
		GzXactionRowMapper mapper = new GzXactionRowMapper();
		PageRowCallbackHandler<GzBaseTransaction> handler = 
					new PageRowCallbackHandler<GzBaseTransaction>( startIndex, endIndex, mapper );
		
	
		String sql = "select * FROM xaction WHERE payer = '" + user.getEmail() + "' OR payee='" + user.getEmail() + "' ORDER BY ID DESC";
				
		log.info(sql);
		GzAccountDaoImpl accountDaoImpl = (GzAccountDaoImpl) accountDao;
		accountDaoImpl.getJdbcTemplate().query(sql,handler);
		return handler.getResults();	
	}

	public Long getCount() {
		return count;
	}

	public void setCount(Long count) {
		this.count = count;
	}

	public int getCurrentPage() {
		return currentPage;
	}

	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}

	public int getPageSize() {
		return pageSize;
	}

	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}

	public void setLastPage(int lastPage) {
		this.lastPage = lastPage;
	}

	public GzBaseUser getUser() {
		return user;
	}

	public void setUser(GzBaseUser user) {
		this.user = user;
	}

}

