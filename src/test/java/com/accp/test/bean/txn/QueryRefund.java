package com.accp.test.bean.txn;

import com.accp.test.bean.BaseBean;

public class QueryRefund extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String refund_seqno;
	private String accp_txno;

	public String getRefund_seqno() {
		return refund_seqno;
	}

	public void setRefund_seqno(String refund_seqno) {
		this.refund_seqno = refund_seqno;
	}

	public String getAccp_txno() {
		return accp_txno;
	}

	public void setAccp_txno(String accp_txno) {
		this.accp_txno = accp_txno;
	}

}