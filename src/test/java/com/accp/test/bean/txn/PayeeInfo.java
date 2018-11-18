package com.accp.test.bean.txn;

import java.io.Serializable;

public class PayeeInfo implements Serializable {
	private static final long serialVersionUID = 1L;
	private String payee_id;
	private String payee_type;
	private String payee_accttype;
	private String payee_amount;
	private String payee_memo;
	private String bank_acctno;
	private String bank_code;
	private String bank_acctname;
	private String cnaps_code;

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

	public String getPayee_amount() {
		return payee_amount;
	}

	public void setPayee_amount(String payee_amount) {
		this.payee_amount = payee_amount;
	}

	public String getPayee_memo() {
		return payee_memo;
	}

	public void setPayee_memo(String payee_memo) {
		this.payee_memo = payee_memo;
	}

	public String getBank_acctno() {
		return bank_acctno;
	}

	public void setBank_acctno(String bank_acctno) {
		this.bank_acctno = bank_acctno;
	}

	public String getBank_code() {
		return bank_code;
	}

	public void setBank_code(String bank_code) {
		this.bank_code = bank_code;
	}

	public String getBank_acctname() {
		return bank_acctname;
	}

	public void setBank_acctname(String bank_acctname) {
		this.bank_acctname = bank_acctname;
	}

	public String getCnaps_code() {
		return cnaps_code;
	}

	public void setCnaps_code(String cnaps_code) {
		this.cnaps_code = cnaps_code;
	}

}