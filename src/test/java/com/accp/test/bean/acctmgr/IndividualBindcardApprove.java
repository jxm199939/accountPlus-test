package com.accp.test.bean.acctmgr;

import com.accp.test.bean.BaseBean;

public class IndividualBindcardApprove extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String user_id;
	private String txn_seqno;
	private String token;
	private String verify_code;



	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}

	public String getTxn_seqno() {
		return txn_seqno;
	}

	public void setTxn_seqno(String txn_seqno) {
		this.txn_seqno = txn_seqno;
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