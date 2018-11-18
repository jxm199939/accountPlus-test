package com.accp.test.bean.txn;

import com.accp.test.bean.BaseBean;

public class ValidationSms extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String payer_type;
	private String payer_id;
	private String txn_seqno;
	private String total_amount;
	private String token;
	private String verify_code;

	public String getPayer_type() {
		return payer_type;
	}

	public void setPayer_type(String payer_type) {
		this.payer_type = payer_type;
	}

	public String getPayer_id() {
		return payer_id;
	}

	public void setPayer_id(String payer_id) {
		this.payer_id = payer_id;
	}

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

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getVerify_code() {
		return verify_code;
	}

	public void setVerify_code(String verify_code) {
		this.verify_code = verify_code;
	}

}