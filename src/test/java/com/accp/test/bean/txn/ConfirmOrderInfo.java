package com.accp.test.bean.txn;

import java.io.Serializable;

public class ConfirmOrderInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String confirm_seqno;
	private String confirm_time;
	private String confirm_amount;

	public String getConfirm_seqno() {
		return confirm_seqno;
	}

	public void setConfirm_seqno(String confirm_seqno) {
		this.confirm_seqno = confirm_seqno;
	}

	public String getConfirm_time() {
		return confirm_time;
	}

	public void setConfirm_time(String confirm_time) {
		this.confirm_time = confirm_time;
	}

	public String getConfirm_amount() {
		return confirm_amount;
	}

	public void setConfirm_amount(String confirm_amount) {
		this.confirm_amount = confirm_amount;
	}

}