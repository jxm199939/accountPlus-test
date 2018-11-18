package com.accp.test.bean.txn;

import java.io.Serializable;

public class PayMethods implements Serializable {
	private static final long serialVersionUID = 1L;
	private String method;
	private String amount;

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}

}