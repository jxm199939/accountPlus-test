package com.accp.test.bean.txn;

import java.io.Serializable;

public class OriginalOrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String txn_seqno;
	private String total_amount;

	public String getTxn_seqno() {
		return txn_seqno;
	}

	public void setTxn_seqno(String txn_seqno) {
		this.txn_seqno = txn_seqno;
	}

	public String getTotal_amount() {
		return total_amount;
	}

	public void setTotal_amount(String total_amount) {
		this.total_amount = total_amount;
	}

}