package org.rp.account.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

import org.rp.account.GzInvoice;
import org.rp.account.GzXaction;
import org.springframework.jdbc.core.RowMapper;

public class GzInvoiceRowMapper implements RowMapper<GzInvoice> {

	@Override
	public GzInvoice mapRow(ResultSet rs, int rowNum) throws SQLException {
	
		GzInvoice invoice = new GzInvoice();
		GzXactionRowMapper.setBaseValues(rs, invoice, GzXaction.XTYPEINVOICE);
		setValues(rs,invoice);
		return invoice;
	}

	public static void setValues(ResultSet rs,GzInvoice invoice) throws SQLException {
		invoice.setParentId(rs.getLong("parentId"));
		Timestamp ts = rs.getTimestamp("paymentdate");
		invoice.setPaymentDate(new Date(ts.getTime()));
		invoice.setRoyalty(rs.getDouble("royalty"));
		invoice.setNetAmount(rs.getDouble("netAmount"));
		String status = rs.getString("status");
		invoice.setStatus(status.charAt(0));
	}
	
}
