package org.rp.account.persistence;


import java.util.List;

import org.apache.log4j.Logger;
import org.rp.account.GzTransaction;
import org.rp.home.persistence.PageRowCallbackHandler;
import org.rp.util.Util;




public class GzTransactionRowMapperPaginated extends GzTransactionRowMapper
{
	private static final Logger log = Logger.getLogger(GzTransactionRowMapperPaginated.class);
	
	private Long count;
	private int currentPage;
	private int lastPage;
	private int pageSize;
	private GzAccountDao accountDao;
	
	public GzTransactionRowMapperPaginated(GzAccountDao accountDao, int pageSize )
	{
		this.accountDao = accountDao;
		count = accountDao.getOutstandingTransactionCount();
		currentPage = 0;
		this.pageSize = pageSize;
		lastPage = (int) (count/pageSize);
		if (count%pageSize>0)
			lastPage++;
		log.info("Created : " + this.toString());
	}
	
	public List<GzTransaction> getNextPage()
	{
		currentPage++;
		return Util.toList(getPage());
	}

	public List<GzTransaction> getPrevPage()
	{
		if (currentPage>0)
			currentPage--;
		return Util.toList(getPage());
	}
	
	public int getLastPage() {
		return lastPage;
	}

	public List<GzTransaction> getPage(int page)
	{
		currentPage=page;
		return Util.toList(getPage());
	}
	
	private Iterable<GzTransaction> getPage()
	{
		long endIndex = currentPage * pageSize;
		long startIndex = endIndex - pageSize;
		endIndex--;
		if (endIndex > count)
			endIndex = count;
		
		log.info("Getting page : " + currentPage + " startIndex : " + startIndex + " endIndex: " + endIndex );
		
		GzTransactionRowMapper mapper = new GzTransactionRowMapper();
		PageRowCallbackHandler<GzTransaction> handler = 
					new PageRowCallbackHandler<GzTransaction>( startIndex, endIndex, mapper );
		
		String sql;
		
		sql = "select * FROM transaction WHERE invoiceid < 0 ORDER BY payer,payee";
				
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

}

