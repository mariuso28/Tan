package org.rp.account.persistence;

public class TransactionIdMap {

	private long tid;
	private long xid;
	
	public TransactionIdMap()
	{
	}

	public long getTid() {
		return tid;
	}

	public void setTid(long tid) {
		this.tid = tid;
	}

	public long getXid() {
		return xid;
	}

	public void setXid(long xid) {
		this.xid = xid;
	}
	
}

