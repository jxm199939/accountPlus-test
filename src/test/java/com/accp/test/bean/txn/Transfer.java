package com.accp.test.bean.txn;

import com.accp.test.bean.BaseBean;

public class Transfer extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String notify_url;
	private String pay_expire;
	private String funds_flag;
	private OrderInfo orderInfo;
	private PayerInfo payerInfo;
	private PayeeInfo payeeInfo;

	public String getNotify_url() {
		return notify_url;
	}

	public void setNotify_url(String notify_url) {
		this.notify_url = notify_url;
	}

	public String getPay_expire() {
		return pay_expire;
	}

	public void setPay_expire(String pay_expire) {
		this.pay_expire = pay_expire;
	}

	public String getFunds_flag() {
		return funds_flag;
	}

	public void setFunds_flag(String funds_flag) {
		this.funds_flag = funds_flag;
	}

	public OrderInfo getOrderInfo() {
		return orderInfo;
	}

	public void setOrderInfo(OrderInfo orderInfo) {
		this.orderInfo = orderInfo;
	}

	public PayerInfo getPayerInfo() {
		return payerInfo;
	}

	public void setPayerInfo(PayerInfo payerInfo) {
		this.payerInfo = payerInfo;
	}

	public PayeeInfo getPayeeInfo() {
		return payeeInfo;
	}

	public void setPayeeInfo(PayeeInfo payeeInfo) {
		this.payeeInfo = payeeInfo;
	}

}