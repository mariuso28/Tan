package org.rp.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.rp.account.GzAccount;
import org.springframework.jdbc.core.RowMapper;

public class GzAccountRowMapper implements RowMapper<GzAccount>{
	
	public GzAccountRowMapper()
	{
	}
	
	public GzAccount mapRow(ResultSet rs,int rowNum) {
	
		try
		{
			GzAccount account = new GzAccount();
			setValues(rs,account);
			return account;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}

	public static void setValues(ResultSet rs,GzAccount account) throws SQLException {
		
		account.setBalance(rs.getDouble("balance"));
		account.setCommission(rs.getDouble("commission"));
	}

}
