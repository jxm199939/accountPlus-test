package com.accp.test.bean.txn;

import java.io.Serializable;

public class RefundOrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String refund_seqno;
	private String refund_time;
	private String payee_id;
	private String payee_type;
	private String payee_accttype;
	private String refund_amount;

	public String getRefund_seqno() {
		return refund_seqno;
	}

	public void setRefund_seqno(String refund_seqno) {
		this.refund_seqno = refund_seqno;
	}

	public String getRefund_time() {
		return refund_time;
	}

	public void setRefund_time(String refund_time) {
		this.refund_time = refund_time;
	}

	public String getPayee_id() {
		return payee_id;
	}

	public void setPayee_id(String payee_id) {
		this.payee_id = payee_id;
	}

	public String getPayee_type() {
		return payee_type;
	}

	public void setPayee_type(String payee_type) {
		this.payee_type = payee_type;
	}

	public String getPayee_accttype() {
		return payee_accttype;
	}

	public void setPayee_accttype(String payee_accttype) {
		this.payee_accttype = payee_accttype;
	}

	public String getRefund_amount() {
		return refund_amount;
	}

	public void setRefund_amount(String refund_amount) {
		this.refund_amount = refund_amount;
	}

}