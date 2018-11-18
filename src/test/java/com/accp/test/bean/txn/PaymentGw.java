package com.accp.test.bean.txn;

import java.util.List;
import com.accp.test.bean.BaseBean;

public class PaymentGw extends BaseBean {
	private static final long serialVersionUID = 1L;
	private String txn_seqno;
	private String total_amount;
	private String risk_item;
	private String appid;
	private String openid;
	private String bankcode;
	private String device_info;
	private String client_ip;
	private PayerInfo payerInfo;
	private List<PayMethods> payMethods;

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

	public String getRisk_item() {
		return risk_item;
	}

	public void setRisk_item(String risk_item) {
		this.risk_item = risk_item;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getBankcode() {
		return bankcode;
	}

	public void setBankcode(String bankcode) {
		this.bankcode = bankcode;
	}

	public String getDevice_info() {
		return device_info;
	}

	public void setDevice_info(String device_info) {
		this.device_info = device_info;
	}

	public String getClient_ip() {
		return client_ip;
	}

	public void setClient_ip(String client_ip) {
		this.client_ip = client_ip;
	}

	public PayerInfo getPayerInfo() {
		return payerInfo;
	}

	public void setPayerInfo(PayerInfo payerInfo) {
		this.payerInfo = payerInfo;
	}

	public List<PayMethods> getPayMethods() {
		return payMethods;
	}

	public void setPayMethods(List<PayMethods> payMethods) {
		this.payMethods = payMethods;
	}

}