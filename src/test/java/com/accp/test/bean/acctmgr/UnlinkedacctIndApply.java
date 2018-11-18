package com.accp.test.bean.acctmgr;

import com.accp.test.bean.BaseBean;

public class UnlinkedacctIndApply extends BaseBean {
	private static final long serialVersionUID = 1L;

	private String user_id;
	private String txn_seqno;
	private String txn_time;
	private String notify_url;
	private String linked_acctno;
	private String password;
	private String random_key;


	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

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

	public String getTxn_time() {
		return txn_time;
	}

	public void setTxn_time(String txn_time) {
		this.txn_time = txn_time;
	}

	public String getLinked_acctno() {
		return linked_acctno;
	}

	public void setLinked_acctno(String linked_acctno) {
		this.linked_acctno = linked_acctno;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRandom_key() {
		return random_key;
	}

	public void setRandom_key(String random_key) {
		this.random_key = random_key;
	}

	
}