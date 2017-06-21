package org.rp.home.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.core.RowMapper;

public class PageRowCallbackHandler<T> implements RowCallbackHandler {

	private final long startIndex;
	private final long endIndex;
	private int index;

	private final RowMapper<T> rowMapper;

	private final List<T> results = new ArrayList<T>();

	public PageRowCallbackHandler(long startIndex, long endIndex,
			RowMapper<T> rowMapper) {
		super();
		this.startIndex = startIndex;
		this.endIndex = endIndex;
		this.rowMapper = rowMapper;
	}

	private boolean rowInPage() {
		return index >= startIndex && index <= endIndex;
	}

	private void addRow(ResultSet resultSet) throws SQLException {
		results.add(rowMapper.mapRow(resultSet, index));
	}

	@Override
	public void processRow(ResultSet resultSet) throws SQLException {
		if (rowInPage())
			addRow(resultSet);
		++index;
	}

	public Iterable<T> getResults() {
		return results;
	}

}
