package com.accp.test.bean.txn;

import java.io.Serializable;

public class PayerInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String user_id;
	private String password;
	private String random_key;
	private String payer_type;
	private String payer_id;
	private String payer_accttype;

	public String getUser_id() {
		return user_id;
	}

	public void setUser_id(String user_id) {
		this.user_id = user_id;
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

	public String getPayer_accttype() {
		return payer_accttype;
	}

	public void setPayer_accttype(String payer_accttype) {
		this.payer_accttype = payer_accttype;
	}

}