package com.accp.test.bean.txn;

import com.accp.test.bean.BaseBean;

public class QueryPayment extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String txn_seqno;
	private String accp_txno;

	public String getTxn_seqno() {
		return txn_seqno;
	}

	public void setTxn_seqno(String txn_seqno) {
		this.txn_seqno = txn_seqno;
	}

	public String getAccp_txno() {
		return accp_txno;
	}

	public void setAccp_txno(String accp_txno) {
		this.accp_txno = accp_txno;
	}

}